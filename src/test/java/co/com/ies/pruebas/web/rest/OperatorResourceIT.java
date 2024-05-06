package co.com.ies.pruebas.web.rest;

import static co.com.ies.pruebas.domain.OperatorAsserts.*;
import static co.com.ies.pruebas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.pruebas.IntegrationTest;
import co.com.ies.pruebas.domain.Operator;
import co.com.ies.pruebas.repository.OperatorRepository;
import co.com.ies.pruebas.service.dto.OperatorDTO;
import co.com.ies.pruebas.service.mapper.OperatorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OperatorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperatorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NIT = "AAAAAAAAAA";
    private static final String UPDATED_NIT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTRACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperatorMockMvc;

    private Operator operator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createEntity(EntityManager em) {
        Operator operator = new Operator().name(DEFAULT_NAME).nit(DEFAULT_NIT).contract(DEFAULT_CONTRACT);
        return operator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operator createUpdatedEntity(EntityManager em) {
        Operator operator = new Operator().name(UPDATED_NAME).nit(UPDATED_NIT).contract(UPDATED_CONTRACT);
        return operator;
    }

    @BeforeEach
    public void initTest() {
        operator = createEntity(em);
    }

    @Test
    @Transactional
    void createOperator() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);
        var returnedOperatorDTO = om.readValue(
            restOperatorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operatorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OperatorDTO.class
        );

        // Validate the Operator in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOperator = operatorMapper.toEntity(returnedOperatorDTO);
        assertOperatorUpdatableFieldsEquals(returnedOperator, getPersistedOperator(returnedOperator));
    }

    @Test
    @Transactional
    void createOperatorWithExistingId() throws Exception {
        // Create the Operator with an existing ID
        operator.setId(1L);
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operator.setName(null);

        // Create the Operator, which fails.
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operator.setNit(null);

        // Create the Operator, which fails.
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContractIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operator.setContract(null);

        // Create the Operator, which fails.
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        restOperatorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operatorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperators() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].contract").value(hasItem(DEFAULT_CONTRACT)));
    }

    @Test
    @Transactional
    void getOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get the operator
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL_ID, operator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operator.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nit").value(DEFAULT_NIT))
            .andExpect(jsonPath("$.contract").value(DEFAULT_CONTRACT));
    }

    @Test
    @Transactional
    void getOperatorsByIdFiltering() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        Long id = operator.getId();

        defaultOperatorFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOperatorFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOperatorFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name equals to
        defaultOperatorFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name in
        defaultOperatorFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name is not null
        defaultOperatorFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByNameContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name contains
        defaultOperatorFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where name does not contain
        defaultOperatorFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit equals to
        defaultOperatorFiltering("nit.equals=" + DEFAULT_NIT, "nit.equals=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit in
        defaultOperatorFiltering("nit.in=" + DEFAULT_NIT + "," + UPDATED_NIT, "nit.in=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit is not null
        defaultOperatorFiltering("nit.specified=true", "nit.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByNitContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit contains
        defaultOperatorFiltering("nit.contains=" + DEFAULT_NIT, "nit.contains=" + UPDATED_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByNitNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where nit does not contain
        defaultOperatorFiltering("nit.doesNotContain=" + UPDATED_NIT, "nit.doesNotContain=" + DEFAULT_NIT);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractIsEqualToSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contract equals to
        defaultOperatorFiltering("contract.equals=" + DEFAULT_CONTRACT, "contract.equals=" + UPDATED_CONTRACT);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractIsInShouldWork() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contract in
        defaultOperatorFiltering("contract.in=" + DEFAULT_CONTRACT + "," + UPDATED_CONTRACT, "contract.in=" + UPDATED_CONTRACT);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractIsNullOrNotNull() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contract is not null
        defaultOperatorFiltering("contract.specified=true", "contract.specified=false");
    }

    @Test
    @Transactional
    void getAllOperatorsByContractContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contract contains
        defaultOperatorFiltering("contract.contains=" + DEFAULT_CONTRACT, "contract.contains=" + UPDATED_CONTRACT);
    }

    @Test
    @Transactional
    void getAllOperatorsByContractNotContainsSomething() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        // Get all the operatorList where contract does not contain
        defaultOperatorFiltering("contract.doesNotContain=" + UPDATED_CONTRACT, "contract.doesNotContain=" + DEFAULT_CONTRACT);
    }

    private void defaultOperatorFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOperatorShouldBeFound(shouldBeFound);
        defaultOperatorShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperatorShouldBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operator.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nit").value(hasItem(DEFAULT_NIT)))
            .andExpect(jsonPath("$.[*].contract").value(hasItem(DEFAULT_CONTRACT)));

        // Check, that the count call also returns 1
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperatorShouldNotBeFound(String filter) throws Exception {
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperatorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOperator() throws Exception {
        // Get the operator
        restOperatorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operator
        Operator updatedOperator = operatorRepository.findById(operator.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOperator are not directly saved in db
        em.detach(updatedOperator);
        updatedOperator.name(UPDATED_NAME).nit(UPDATED_NIT).contract(UPDATED_CONTRACT);
        OperatorDTO operatorDTO = operatorMapper.toDto(updatedOperator);

        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operatorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOperatorToMatchAllProperties(updatedOperator);
    }

    @Test
    @Transactional
    void putNonExistingOperator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operator.setId(longCount.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operator.setId(longCount.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operator.setId(longCount.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operatorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        partialUpdatedOperator.nit(UPDATED_NIT).contract(UPDATED_CONTRACT);

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOperatorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOperator, operator), getPersistedOperator(operator));
    }

    @Test
    @Transactional
    void fullUpdateOperatorWithPatch() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operator using partial update
        Operator partialUpdatedOperator = new Operator();
        partialUpdatedOperator.setId(operator.getId());

        partialUpdatedOperator.name(UPDATED_NAME).nit(UPDATED_NIT).contract(UPDATED_CONTRACT);

        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperator.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOperator))
            )
            .andExpect(status().isOk());

        // Validate the Operator in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOperatorUpdatableFieldsEquals(partialUpdatedOperator, getPersistedOperator(partialUpdatedOperator));
    }

    @Test
    @Transactional
    void patchNonExistingOperator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operator.setId(longCount.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operatorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operator.setId(longCount.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(operatorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperator() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operator.setId(longCount.incrementAndGet());

        // Create the Operator
        OperatorDTO operatorDTO = operatorMapper.toDto(operator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(operatorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operator in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperator() throws Exception {
        // Initialize the database
        operatorRepository.saveAndFlush(operator);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the operator
        restOperatorMockMvc
            .perform(delete(ENTITY_API_URL_ID, operator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return operatorRepository.count();
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

    protected Operator getPersistedOperator(Operator operator) {
        return operatorRepository.findById(operator.getId()).orElseThrow();
    }

    protected void assertPersistedOperatorToMatchAllProperties(Operator expectedOperator) {
        assertOperatorAllPropertiesEquals(expectedOperator, getPersistedOperator(expectedOperator));
    }

    protected void assertPersistedOperatorToMatchUpdatableProperties(Operator expectedOperator) {
        assertOperatorAllUpdatablePropertiesEquals(expectedOperator, getPersistedOperator(expectedOperator));
    }
}
