package co.com.ies.pruebas.web.rest;

import static co.com.ies.pruebas.domain.CasinoAsserts.*;
import static co.com.ies.pruebas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.pruebas.IntegrationTest;
import co.com.ies.pruebas.domain.Casino;
import co.com.ies.pruebas.domain.Operator;
import co.com.ies.pruebas.repository.CasinoRepository;
import co.com.ies.pruebas.service.CasinoService;
import co.com.ies.pruebas.service.dto.CasinoDTO;
import co.com.ies.pruebas.service.mapper.CasinoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CasinoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CasinoResourceIT {

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/casinos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CasinoRepository casinoRepository;

    @Mock
    private CasinoRepository casinoRepositoryMock;

    @Autowired
    private CasinoMapper casinoMapper;

    @Mock
    private CasinoService casinoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCasinoMockMvc;

    private Casino casino;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Casino createEntity(EntityManager em) {
        Casino casino = new Casino().nit(DEFAULT_NIT).name(DEFAULT_NAME).direction(DEFAULT_DIRECTION);
        return casino;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Casino createUpdatedEntity(EntityManager em) {
        Casino casino = new Casino().nit(UPDATED_NIT).name(UPDATED_NAME).direction(UPDATED_DIRECTION);
        return casino;
    }

    @BeforeEach
    public void initTest() {
        casino = createEntity(em);
    }

    @Test
    @Transactional
    void createCasino() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Casino
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);
        var returnedCasinoDTO = om.readValue(
            restCasinoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CasinoDTO.class
        );

        // Validate the Casino in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCasino = casinoMapper.toEntity(returnedCasinoDTO);
        assertCasinoUpdatableFieldsEquals(returnedCasino, getPersistedCasino(returnedCasino));
    }

    @Test
    @Transactional
    void createCasinoWithExistingId() throws Exception {
        // Create the Casino with an existing ID
        casino.setId(1L);
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCasinoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        casino.setNit(null);

        // Create the Casino, which fails.
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        restCasinoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        casino.setName(null);

        // Create the Casino, which fails.
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        restCasinoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        casino.setDirection(null);

        // Create the Casino, which fails.
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        restCasinoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCasinos() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList
        restCasinoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casino.getId().intValue())))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCasinosWithEagerRelationshipsIsEnabled() throws Exception {
        when(casinoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCasinoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(casinoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCasinosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(casinoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCasinoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(casinoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCasino() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get the casino
        restCasinoMockMvc
            .perform(get(ENTITY_API_URL_ID, casino.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(casino.getId().intValue()))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION));
    }

    @Test
    @Transactional
    void getCasinosByIdFiltering() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        Long id = casino.getId();

        defaultCasinoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCasinoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCasinoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCasinosByNitIsEqualToSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where nit equals to
        defaultCasinoFiltering("nit.equals=" + DEFAULT_NIT, "nit.equals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllCasinosByNitIsInShouldWork() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where nit in
        defaultCasinoFiltering("nit.in=" + DEFAULT_NIT + "," + UPDATED_NIT, "nit.in=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllCasinosByNitIsNullOrNotNull() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where nit is not null
        defaultCasinoFiltering("nit.specified=true", "nit.specified=false");
    }

    @Test
    @Transactional
    void getAllCasinosByNitContainsSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where nit contains
        defaultCasinoFiltering("nit.contains=" + DEFAULT_NIT, "nit.contains=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllCasinosByNitNotContainsSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where nit does not contain
        defaultCasinoFiltering("nit.doesNotContain=" + UPDATED_NIT, "nit.doesNotContain=" + DEFAULT_NIT);
    }

    @Test
    @Transactional
    void getAllCasinosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where name equals to
        defaultCasinoFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCasinosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where name in
        defaultCasinoFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCasinosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where name is not null
        defaultCasinoFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCasinosByNameContainsSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where name contains
        defaultCasinoFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCasinosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where name does not contain
        defaultCasinoFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCasinosByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where direction equals to
        defaultCasinoFiltering("direction.equals=" + DEFAULT_DIRECTION, "direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllCasinosByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where direction in
        defaultCasinoFiltering("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION, "direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllCasinosByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where direction is not null
        defaultCasinoFiltering("direction.specified=true", "direction.specified=false");
    }

    @Test
    @Transactional
    void getAllCasinosByDirectionContainsSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where direction contains
        defaultCasinoFiltering("direction.contains=" + DEFAULT_DIRECTION, "direction.contains=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllCasinosByDirectionNotContainsSomething() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        // Get all the casinoList where direction does not contain
        defaultCasinoFiltering("direction.doesNotContain=" + UPDATED_DIRECTION, "direction.doesNotContain=" + DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void getAllCasinosByOperatorIsEqualToSomething() throws Exception {
        Operator operator;
        if (TestUtil.findAll(em, Operator.class).isEmpty()) {
            casinoRepository.saveAndFlush(casino);
            operator = OperatorResourceIT.createEntity(em);
        } else {
            operator = TestUtil.findAll(em, Operator.class).get(0);
        }
        em.persist(operator);
        em.flush();
        casino.setOperator(operator);
        casinoRepository.saveAndFlush(casino);
        Long operatorId = operator.getId();
        // Get all the casinoList where operator equals to operatorId
        defaultCasinoShouldBeFound("operatorId.equals=" + operatorId);

        // Get all the casinoList where operator equals to (operatorId + 1)
        defaultCasinoShouldNotBeFound("operatorId.equals=" + (operatorId + 1));
    }

    private void defaultCasinoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCasinoShouldBeFound(shouldBeFound);
        defaultCasinoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCasinoShouldBeFound(String filter) throws Exception {
        restCasinoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(casino.getId().intValue())))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION)));

        // Check, that the count call also returns 1
        restCasinoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCasinoShouldNotBeFound(String filter) throws Exception {
        restCasinoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCasinoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCasino() throws Exception {
        // Get the casino
        restCasinoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCasino() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the casino
        Casino updatedCasino = casinoRepository.findById(casino.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCasino are not directly saved in db
        em.detach(updatedCasino);
        updatedCasino.nit(UPDATED_NIT).name(UPDATED_NAME).direction(UPDATED_DIRECTION);
        CasinoDTO casinoDTO = casinoMapper.toDto(updatedCasino);

        restCasinoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casinoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCasinoToMatchAllProperties(updatedCasino);
    }

    @Test
    @Transactional
    void putNonExistingCasino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        casino.setId(longCount.incrementAndGet());

        // Create the Casino
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasinoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, casinoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCasino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        casino.setId(longCount.incrementAndGet());

        // Create the Casino
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasinoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(casinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCasino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        casino.setId(longCount.incrementAndGet());

        // Create the Casino
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasinoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(casinoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCasinoWithPatch() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the casino using partial update
        Casino partialUpdatedCasino = new Casino();
        partialUpdatedCasino.setId(casino.getId());

        partialUpdatedCasino.nit(UPDATED_NIT).name(UPDATED_NAME);

        restCasinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasino.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCasino))
            )
            .andExpect(status().isOk());

        // Validate the Casino in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCasinoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCasino, casino), getPersistedCasino(casino));
    }

    @Test
    @Transactional
    void fullUpdateCasinoWithPatch() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the casino using partial update
        Casino partialUpdatedCasino = new Casino();
        partialUpdatedCasino.setId(casino.getId());

        partialUpdatedCasino.nit(UPDATED_NIT).name(UPDATED_NAME).direction(UPDATED_DIRECTION);

        restCasinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCasino.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCasino))
            )
            .andExpect(status().isOk());

        // Validate the Casino in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCasinoUpdatableFieldsEquals(partialUpdatedCasino, getPersistedCasino(partialUpdatedCasino));
    }

    @Test
    @Transactional
    void patchNonExistingCasino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        casino.setId(longCount.incrementAndGet());

        // Create the Casino
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCasinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, casinoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(casinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCasino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        casino.setId(longCount.incrementAndGet());

        // Create the Casino
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasinoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(casinoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCasino() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        casino.setId(longCount.incrementAndGet());

        // Create the Casino
        CasinoDTO casinoDTO = casinoMapper.toDto(casino);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCasinoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(casinoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Casino in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCasino() throws Exception {
        // Initialize the database
        casinoRepository.saveAndFlush(casino);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the casino
        restCasinoMockMvc
            .perform(delete(ENTITY_API_URL_ID, casino.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return casinoRepository.count();
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

    protected Casino getPersistedCasino(Casino casino) {
        return casinoRepository.findById(casino.getId()).orElseThrow();
    }

    protected void assertPersistedCasinoToMatchAllProperties(Casino expectedCasino) {
        assertCasinoAllPropertiesEquals(expectedCasino, getPersistedCasino(expectedCasino));
    }

    protected void assertPersistedCasinoToMatchUpdatableProperties(Casino expectedCasino) {
        assertCasinoAllUpdatablePropertiesEquals(expectedCasino, getPersistedCasino(expectedCasino));
    }
}
