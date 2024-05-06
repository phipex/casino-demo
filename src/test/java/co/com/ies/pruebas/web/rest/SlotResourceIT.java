package co.com.ies.pruebas.web.rest;

import static co.com.ies.pruebas.domain.SlotAsserts.*;
import static co.com.ies.pruebas.web.rest.TestUtil.createUpdateProxyForBean;
import static co.com.ies.pruebas.web.rest.TestUtil.sameInstant;
import static co.com.ies.pruebas.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.pruebas.IntegrationTest;
import co.com.ies.pruebas.domain.Casino;
import co.com.ies.pruebas.domain.Model;
import co.com.ies.pruebas.domain.Slot;
import co.com.ies.pruebas.repository.SlotRepository;
import co.com.ies.pruebas.service.SlotService;
import co.com.ies.pruebas.service.dto.SlotDTO;
import co.com.ies.pruebas.service.mapper.SlotMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SlotResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SlotResourceIT {

    private static final Integer DEFAULT_ID_CASINO = 1;
    private static final Integer UPDATED_ID_CASINO = 2;
    private static final Integer SMALLER_ID_CASINO = 1 - 1;

    private static final String DEFAULT_SERIAL = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL = "BBBBBBBBBB";

    private static final String DEFAULT_NUC = "AAAAAAAAAA";
    private static final String UPDATED_NUC = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_INITIALIZED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INITIALIZED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_INITIALIZED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/slots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SlotRepository slotRepository;

    @Mock
    private SlotRepository slotRepositoryMock;

    @Autowired
    private SlotMapper slotMapper;

    @Mock
    private SlotService slotServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSlotMockMvc;

    private Slot slot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slot createEntity(EntityManager em) {
        Slot slot = new Slot()
            .idCasino(DEFAULT_ID_CASINO)
            .serial(DEFAULT_SERIAL)
            .nuc(DEFAULT_NUC)
            .initialized(DEFAULT_INITIALIZED)
            .balance(DEFAULT_BALANCE);
        return slot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Slot createUpdatedEntity(EntityManager em) {
        Slot slot = new Slot()
            .idCasino(UPDATED_ID_CASINO)
            .serial(UPDATED_SERIAL)
            .nuc(UPDATED_NUC)
            .initialized(UPDATED_INITIALIZED)
            .balance(UPDATED_BALANCE);
        return slot;
    }

    @BeforeEach
    public void initTest() {
        slot = createEntity(em);
    }

    @Test
    @Transactional
    void createSlot() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);
        var returnedSlotDTO = om.readValue(
            restSlotMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SlotDTO.class
        );

        // Validate the Slot in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSlot = slotMapper.toEntity(returnedSlotDTO);
        assertSlotUpdatableFieldsEquals(returnedSlot, getPersistedSlot(returnedSlot));
    }

    @Test
    @Transactional
    void createSlotWithExistingId() throws Exception {
        // Create the Slot with an existing ID
        slot.setId(1L);
        SlotDTO slotDTO = slotMapper.toDto(slot);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdCasinoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slot.setIdCasino(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);

        restSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSerialIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slot.setSerial(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);

        restSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNucIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slot.setNuc(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);

        restSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInitializedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        slot.setInitialized(null);

        // Create the Slot, which fails.
        SlotDTO slotDTO = slotMapper.toDto(slot);

        restSlotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSlots() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList
        restSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slot.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCasino").value(hasItem(DEFAULT_ID_CASINO)))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)))
            .andExpect(jsonPath("$.[*].nuc").value(hasItem(DEFAULT_NUC)))
            .andExpect(jsonPath("$.[*].initialized").value(hasItem(sameInstant(DEFAULT_INITIALIZED))))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSlotsWithEagerRelationshipsIsEnabled() throws Exception {
        when(slotServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSlotMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(slotServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSlotsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(slotServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSlotMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(slotRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get the slot
        restSlotMockMvc
            .perform(get(ENTITY_API_URL_ID, slot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(slot.getId().intValue()))
            .andExpect(jsonPath("$.idCasino").value(DEFAULT_ID_CASINO))
            .andExpect(jsonPath("$.serial").value(DEFAULT_SERIAL))
            .andExpect(jsonPath("$.nuc").value(DEFAULT_NUC))
            .andExpect(jsonPath("$.initialized").value(sameInstant(DEFAULT_INITIALIZED)))
            .andExpect(jsonPath("$.balance").value(sameNumber(DEFAULT_BALANCE)));
    }

    @Test
    @Transactional
    void getSlotsByIdFiltering() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        Long id = slot.getId();

        defaultSlotFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSlotFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSlotFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSlotsByIdCasinoIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where idCasino equals to
        defaultSlotFiltering("idCasino.equals=" + DEFAULT_ID_CASINO, "idCasino.equals=" + UPDATED_ID_CASINO);
    }

    @Test
    @Transactional
    void getAllSlotsByIdCasinoIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where idCasino in
        defaultSlotFiltering("idCasino.in=" + DEFAULT_ID_CASINO + "," + UPDATED_ID_CASINO, "idCasino.in=" + UPDATED_ID_CASINO);
    }

    @Test
    @Transactional
    void getAllSlotsByIdCasinoIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where idCasino is not null
        defaultSlotFiltering("idCasino.specified=true", "idCasino.specified=false");
    }

    @Test
    @Transactional
    void getAllSlotsByIdCasinoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where idCasino is greater than or equal to
        defaultSlotFiltering("idCasino.greaterThanOrEqual=" + DEFAULT_ID_CASINO, "idCasino.greaterThanOrEqual=" + UPDATED_ID_CASINO);
    }

    @Test
    @Transactional
    void getAllSlotsByIdCasinoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where idCasino is less than or equal to
        defaultSlotFiltering("idCasino.lessThanOrEqual=" + DEFAULT_ID_CASINO, "idCasino.lessThanOrEqual=" + SMALLER_ID_CASINO);
    }

    @Test
    @Transactional
    void getAllSlotsByIdCasinoIsLessThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where idCasino is less than
        defaultSlotFiltering("idCasino.lessThan=" + UPDATED_ID_CASINO, "idCasino.lessThan=" + DEFAULT_ID_CASINO);
    }

    @Test
    @Transactional
    void getAllSlotsByIdCasinoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where idCasino is greater than
        defaultSlotFiltering("idCasino.greaterThan=" + SMALLER_ID_CASINO, "idCasino.greaterThan=" + DEFAULT_ID_CASINO);
    }

    @Test
    @Transactional
    void getAllSlotsBySerialIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where serial equals to
        defaultSlotFiltering("serial.equals=" + DEFAULT_SERIAL, "serial.equals=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllSlotsBySerialIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where serial in
        defaultSlotFiltering("serial.in=" + DEFAULT_SERIAL + "," + UPDATED_SERIAL, "serial.in=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllSlotsBySerialIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where serial is not null
        defaultSlotFiltering("serial.specified=true", "serial.specified=false");
    }

    @Test
    @Transactional
    void getAllSlotsBySerialContainsSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where serial contains
        defaultSlotFiltering("serial.contains=" + DEFAULT_SERIAL, "serial.contains=" + UPDATED_SERIAL);
    }

    @Test
    @Transactional
    void getAllSlotsBySerialNotContainsSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where serial does not contain
        defaultSlotFiltering("serial.doesNotContain=" + UPDATED_SERIAL, "serial.doesNotContain=" + DEFAULT_SERIAL);
    }

    @Test
    @Transactional
    void getAllSlotsByNucIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where nuc equals to
        defaultSlotFiltering("nuc.equals=" + DEFAULT_NUC, "nuc.equals=" + UPDATED_NUC);
    }

    @Test
    @Transactional
    void getAllSlotsByNucIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where nuc in
        defaultSlotFiltering("nuc.in=" + DEFAULT_NUC + "," + UPDATED_NUC, "nuc.in=" + UPDATED_NUC);
    }

    @Test
    @Transactional
    void getAllSlotsByNucIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where nuc is not null
        defaultSlotFiltering("nuc.specified=true", "nuc.specified=false");
    }

    @Test
    @Transactional
    void getAllSlotsByNucContainsSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where nuc contains
        defaultSlotFiltering("nuc.contains=" + DEFAULT_NUC, "nuc.contains=" + UPDATED_NUC);
    }

    @Test
    @Transactional
    void getAllSlotsByNucNotContainsSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where nuc does not contain
        defaultSlotFiltering("nuc.doesNotContain=" + UPDATED_NUC, "nuc.doesNotContain=" + DEFAULT_NUC);
    }

    @Test
    @Transactional
    void getAllSlotsByInitializedIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where initialized equals to
        defaultSlotFiltering("initialized.equals=" + DEFAULT_INITIALIZED, "initialized.equals=" + UPDATED_INITIALIZED);
    }

    @Test
    @Transactional
    void getAllSlotsByInitializedIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where initialized in
        defaultSlotFiltering("initialized.in=" + DEFAULT_INITIALIZED + "," + UPDATED_INITIALIZED, "initialized.in=" + UPDATED_INITIALIZED);
    }

    @Test
    @Transactional
    void getAllSlotsByInitializedIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where initialized is not null
        defaultSlotFiltering("initialized.specified=true", "initialized.specified=false");
    }

    @Test
    @Transactional
    void getAllSlotsByInitializedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where initialized is greater than or equal to
        defaultSlotFiltering(
            "initialized.greaterThanOrEqual=" + DEFAULT_INITIALIZED,
            "initialized.greaterThanOrEqual=" + UPDATED_INITIALIZED
        );
    }

    @Test
    @Transactional
    void getAllSlotsByInitializedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where initialized is less than or equal to
        defaultSlotFiltering("initialized.lessThanOrEqual=" + DEFAULT_INITIALIZED, "initialized.lessThanOrEqual=" + SMALLER_INITIALIZED);
    }

    @Test
    @Transactional
    void getAllSlotsByInitializedIsLessThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where initialized is less than
        defaultSlotFiltering("initialized.lessThan=" + UPDATED_INITIALIZED, "initialized.lessThan=" + DEFAULT_INITIALIZED);
    }

    @Test
    @Transactional
    void getAllSlotsByInitializedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where initialized is greater than
        defaultSlotFiltering("initialized.greaterThan=" + SMALLER_INITIALIZED, "initialized.greaterThan=" + DEFAULT_INITIALIZED);
    }

    @Test
    @Transactional
    void getAllSlotsByBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where balance equals to
        defaultSlotFiltering("balance.equals=" + DEFAULT_BALANCE, "balance.equals=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllSlotsByBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where balance in
        defaultSlotFiltering("balance.in=" + DEFAULT_BALANCE + "," + UPDATED_BALANCE, "balance.in=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllSlotsByBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where balance is not null
        defaultSlotFiltering("balance.specified=true", "balance.specified=false");
    }

    @Test
    @Transactional
    void getAllSlotsByBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where balance is greater than or equal to
        defaultSlotFiltering("balance.greaterThanOrEqual=" + DEFAULT_BALANCE, "balance.greaterThanOrEqual=" + UPDATED_BALANCE);
    }

    @Test
    @Transactional
    void getAllSlotsByBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where balance is less than or equal to
        defaultSlotFiltering("balance.lessThanOrEqual=" + DEFAULT_BALANCE, "balance.lessThanOrEqual=" + SMALLER_BALANCE);
    }

    @Test
    @Transactional
    void getAllSlotsByBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where balance is less than
        defaultSlotFiltering("balance.lessThan=" + UPDATED_BALANCE, "balance.lessThan=" + DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void getAllSlotsByBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        // Get all the slotList where balance is greater than
        defaultSlotFiltering("balance.greaterThan=" + SMALLER_BALANCE, "balance.greaterThan=" + DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    void getAllSlotsByCasinoIsEqualToSomething() throws Exception {
        Casino casino;
        if (TestUtil.findAll(em, Casino.class).isEmpty()) {
            slotRepository.saveAndFlush(slot);
            casino = CasinoResourceIT.createEntity(em);
        } else {
            casino = TestUtil.findAll(em, Casino.class).get(0);
        }
        em.persist(casino);
        em.flush();
        slot.setCasino(casino);
        slotRepository.saveAndFlush(slot);
        Long casinoId = casino.getId();
        // Get all the slotList where casino equals to casinoId
        defaultSlotShouldBeFound("casinoId.equals=" + casinoId);

        // Get all the slotList where casino equals to (casinoId + 1)
        defaultSlotShouldNotBeFound("casinoId.equals=" + (casinoId + 1));
    }

    @Test
    @Transactional
    void getAllSlotsByModelIsEqualToSomething() throws Exception {
        Model model;
        if (TestUtil.findAll(em, Model.class).isEmpty()) {
            slotRepository.saveAndFlush(slot);
            model = ModelResourceIT.createEntity(em);
        } else {
            model = TestUtil.findAll(em, Model.class).get(0);
        }
        em.persist(model);
        em.flush();
        slot.setModel(model);
        slotRepository.saveAndFlush(slot);
        Long modelId = model.getId();
        // Get all the slotList where model equals to modelId
        defaultSlotShouldBeFound("modelId.equals=" + modelId);

        // Get all the slotList where model equals to (modelId + 1)
        defaultSlotShouldNotBeFound("modelId.equals=" + (modelId + 1));
    }

    private void defaultSlotFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSlotShouldBeFound(shouldBeFound);
        defaultSlotShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSlotShouldBeFound(String filter) throws Exception {
        restSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slot.getId().intValue())))
            .andExpect(jsonPath("$.[*].idCasino").value(hasItem(DEFAULT_ID_CASINO)))
            .andExpect(jsonPath("$.[*].serial").value(hasItem(DEFAULT_SERIAL)))
            .andExpect(jsonPath("$.[*].nuc").value(hasItem(DEFAULT_NUC)))
            .andExpect(jsonPath("$.[*].initialized").value(hasItem(sameInstant(DEFAULT_INITIALIZED))))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(sameNumber(DEFAULT_BALANCE))));

        // Check, that the count call also returns 1
        restSlotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSlotShouldNotBeFound(String filter) throws Exception {
        restSlotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSlotMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSlot() throws Exception {
        // Get the slot
        restSlotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slot
        Slot updatedSlot = slotRepository.findById(slot.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSlot are not directly saved in db
        em.detach(updatedSlot);
        updatedSlot
            .idCasino(UPDATED_ID_CASINO)
            .serial(UPDATED_SERIAL)
            .nuc(UPDATED_NUC)
            .initialized(UPDATED_INITIALIZED)
            .balance(UPDATED_BALANCE);
        SlotDTO slotDTO = slotMapper.toDto(updatedSlot);

        restSlotMockMvc
            .perform(put(ENTITY_API_URL_ID, slotDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isOk());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSlotToMatchAllProperties(updatedSlot);
    }

    @Test
    @Transactional
    void putNonExistingSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slot.setId(longCount.incrementAndGet());

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotMockMvc
            .perform(put(ENTITY_API_URL_ID, slotDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slot.setId(longCount.incrementAndGet());

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(slotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slot.setId(longCount.incrementAndGet());

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSlotWithPatch() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slot using partial update
        Slot partialUpdatedSlot = new Slot();
        partialUpdatedSlot.setId(slot.getId());

        partialUpdatedSlot.initialized(UPDATED_INITIALIZED).balance(UPDATED_BALANCE);

        restSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlot))
            )
            .andExpect(status().isOk());

        // Validate the Slot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSlot, slot), getPersistedSlot(slot));
    }

    @Test
    @Transactional
    void fullUpdateSlotWithPatch() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the slot using partial update
        Slot partialUpdatedSlot = new Slot();
        partialUpdatedSlot.setId(slot.getId());

        partialUpdatedSlot
            .idCasino(UPDATED_ID_CASINO)
            .serial(UPDATED_SERIAL)
            .nuc(UPDATED_NUC)
            .initialized(UPDATED_INITIALIZED)
            .balance(UPDATED_BALANCE);

        restSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSlot.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSlot))
            )
            .andExpect(status().isOk());

        // Validate the Slot in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSlotUpdatableFieldsEquals(partialUpdatedSlot, getPersistedSlot(partialUpdatedSlot));
    }

    @Test
    @Transactional
    void patchNonExistingSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slot.setId(longCount.incrementAndGet());

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, slotDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(slotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slot.setId(longCount.incrementAndGet());

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(slotDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSlot() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        slot.setId(longCount.incrementAndGet());

        // Create the Slot
        SlotDTO slotDTO = slotMapper.toDto(slot);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSlotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(slotDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Slot in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSlot() throws Exception {
        // Initialize the database
        slotRepository.saveAndFlush(slot);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the slot
        restSlotMockMvc
            .perform(delete(ENTITY_API_URL_ID, slot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return slotRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Slot getPersistedSlot(Slot slot) {
        return slotRepository.findById(slot.getId()).orElseThrow();
    }

    protected void assertPersistedSlotToMatchAllProperties(Slot expectedSlot) {
        assertSlotAllPropertiesEquals(expectedSlot, getPersistedSlot(expectedSlot));
    }

    protected void assertPersistedSlotToMatchUpdatableProperties(Slot expectedSlot) {
        assertSlotAllUpdatablePropertiesEquals(expectedSlot, getPersistedSlot(expectedSlot));
    }
}
