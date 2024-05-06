package co.com.ies.pruebas.web.rest;

import static co.com.ies.pruebas.domain.ModelAsserts.*;
import static co.com.ies.pruebas.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.ies.pruebas.IntegrationTest;
import co.com.ies.pruebas.domain.Manufacturer;
import co.com.ies.pruebas.domain.Model;
import co.com.ies.pruebas.repository.ModelRepository;
import co.com.ies.pruebas.service.ModelService;
import co.com.ies.pruebas.service.dto.ModelDTO;
import co.com.ies.pruebas.service.mapper.ModelMapper;
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
 * Integration tests for the {@link ModelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModelRepository modelRepository;

    @Mock
    private ModelRepository modelRepositoryMock;

    @Autowired
    private ModelMapper modelMapper;

    @Mock
    private ModelService modelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModelMockMvc;

    private Model model;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createEntity(EntityManager em) {
        Model model = new Model().name(DEFAULT_NAME);
        return model;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createUpdatedEntity(EntityManager em) {
        Model model = new Model().name(UPDATED_NAME);
        return model;
    }

    @BeforeEach
    public void initTest() {
        model = createEntity(em);
    }

    @Test
    @Transactional
    void createModel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);
        var returnedModelDTO = om.readValue(
            restModelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ModelDTO.class
        );

        // Validate the Model in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModel = modelMapper.toEntity(returnedModelDTO);
        assertModelUpdatableFieldsEquals(returnedModel, getPersistedModel(returnedModel));
    }

    @Test
    @Transactional
    void createModelWithExistingId() throws Exception {
        // Create the Model with an existing ID
        model.setId(1L);
        ModelDTO modelDTO = modelMapper.toDto(model);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        model.setName(null);

        // Create the Model, which fails.
        ModelDTO modelDTO = modelMapper.toDto(model);

        restModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(modelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(modelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(modelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(modelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get the model
        restModelMockMvc
            .perform(get(ENTITY_API_URL_ID, model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(model.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getModelsByIdFiltering() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        Long id = model.getId();

        defaultModelFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultModelFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultModelFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllModelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name equals to
        defaultModelFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllModelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name in
        defaultModelFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllModelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name is not null
        defaultModelFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllModelsByNameContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name contains
        defaultModelFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllModelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the modelList where name does not contain
        defaultModelFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllModelsByManufacturerIsEqualToSomething() throws Exception {
        Manufacturer manufacturer;
        if (TestUtil.findAll(em, Manufacturer.class).isEmpty()) {
            modelRepository.saveAndFlush(model);
            manufacturer = ManufacturerResourceIT.createEntity(em);
        } else {
            manufacturer = TestUtil.findAll(em, Manufacturer.class).get(0);
        }
        em.persist(manufacturer);
        em.flush();
        model.setManufacturer(manufacturer);
        modelRepository.saveAndFlush(model);
        Long manufacturerId = manufacturer.getId();
        // Get all the modelList where manufacturer equals to manufacturerId
        defaultModelShouldBeFound("manufacturerId.equals=" + manufacturerId);

        // Get all the modelList where manufacturer equals to (manufacturerId + 1)
        defaultModelShouldNotBeFound("manufacturerId.equals=" + (manufacturerId + 1));
    }

    private void defaultModelFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultModelShouldBeFound(shouldBeFound);
        defaultModelShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultModelShouldBeFound(String filter) throws Exception {
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultModelShouldNotBeFound(String filter) throws Exception {
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the model
        Model updatedModel = modelRepository.findById(model.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModel are not directly saved in db
        em.detach(updatedModel);
        updatedModel.name(UPDATED_NAME);
        ModelDTO modelDTO = modelMapper.toDto(updatedModel);

        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modelDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelDTO))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModelToMatchAllProperties(updatedModel);
    }

    @Test
    @Transactional
    void putNonExistingModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        model.setId(longCount.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, modelDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        model.setId(longCount.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        model.setId(longCount.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(modelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModelWithPatch() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel.name(UPDATED_NAME);

        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModel))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedModel, model), getPersistedModel(model));
    }

    @Test
    @Transactional
    void fullUpdateModelWithPatch() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the model using partial update
        Model partialUpdatedModel = new Model();
        partialUpdatedModel.setId(model.getId());

        partialUpdatedModel.name(UPDATED_NAME);

        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModel))
            )
            .andExpect(status().isOk());

        // Validate the Model in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModelUpdatableFieldsEquals(partialUpdatedModel, getPersistedModel(partialUpdatedModel));
    }

    @Test
    @Transactional
    void patchNonExistingModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        model.setId(longCount.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, modelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        model.setId(longCount.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(modelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        model.setId(longCount.incrementAndGet());

        // Create the Model
        ModelDTO modelDTO = modelMapper.toDto(model);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(modelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Model in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the model
        restModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, model.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return modelRepository.count();
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

    protected Model getPersistedModel(Model model) {
        return modelRepository.findById(model.getId()).orElseThrow();
    }

    protected void assertPersistedModelToMatchAllProperties(Model expectedModel) {
        assertModelAllPropertiesEquals(expectedModel, getPersistedModel(expectedModel));
    }

    protected void assertPersistedModelToMatchUpdatableProperties(Model expectedModel) {
        assertModelAllUpdatablePropertiesEquals(expectedModel, getPersistedModel(expectedModel));
    }
}
