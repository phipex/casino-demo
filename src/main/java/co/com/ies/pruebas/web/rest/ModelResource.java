package co.com.ies.pruebas.web.rest;

import co.com.ies.pruebas.repository.ModelRepository;
import co.com.ies.pruebas.service.ModelQueryService;
import co.com.ies.pruebas.service.ModelService;
import co.com.ies.pruebas.service.criteria.ModelCriteria;
import co.com.ies.pruebas.service.dto.ModelDTO;
import co.com.ies.pruebas.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.com.ies.pruebas.domain.Model}.
 */
@RestController
@RequestMapping("/api/models")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    private static final String ENTITY_NAME = "model";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ModelService modelService;

    private final ModelRepository modelRepository;

    private final ModelQueryService modelQueryService;

    public ModelResource(ModelService modelService, ModelRepository modelRepository, ModelQueryService modelQueryService) {
        this.modelService = modelService;
        this.modelRepository = modelRepository;
        this.modelQueryService = modelQueryService;
    }

    /**
     * {@code POST  /models} : Create a new model.
     *
     * @param modelDTO the modelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new modelDTO, or with status {@code 400 (Bad Request)} if the model has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ModelDTO> createModel(@Valid @RequestBody ModelDTO modelDTO) throws URISyntaxException {
        log.debug("REST request to save Model : {}", modelDTO);
        if (modelDTO.getId() != null) {
            throw new BadRequestAlertException("A new model cannot already have an ID", ENTITY_NAME, "idexists");
        }
        modelDTO = modelService.save(modelDTO);
        return ResponseEntity.created(new URI("/api/models/" + modelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, modelDTO.getId().toString()))
            .body(modelDTO);
    }

    /**
     * {@code PUT  /models/:id} : Updates an existing model.
     *
     * @param id the id of the modelDTO to save.
     * @param modelDTO the modelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelDTO,
     * or with status {@code 400 (Bad Request)} if the modelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the modelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ModelDTO> updateModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModelDTO modelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Model : {}, {}", id, modelDTO);
        if (modelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        modelDTO = modelService.update(modelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, modelDTO.getId().toString()))
            .body(modelDTO);
    }

    /**
     * {@code PATCH  /models/:id} : Partial updates given fields of an existing model, field will ignore if it is null
     *
     * @param id the id of the modelDTO to save.
     * @param modelDTO the modelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated modelDTO,
     * or with status {@code 400 (Bad Request)} if the modelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the modelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the modelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ModelDTO> partialUpdateModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModelDTO modelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Model partially : {}, {}", id, modelDTO);
        if (modelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, modelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!modelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModelDTO> result = modelService.partialUpdate(modelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, modelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /models} : get all the models.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of models in body.
     * @throws Exception
     */
    @GetMapping("")
    public ResponseEntity<List<ModelDTO>> getAllModels(
        ModelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        log.debug("REST request to get Models by criteria: {}", criteria);
        RandomDelay.randomDelay(true);
        Page<ModelDTO> page = modelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /models/count} : count all the models.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countModels(ModelCriteria criteria) {
        log.debug("REST request to count Models by criteria: {}", criteria);
        return ResponseEntity.ok().body(modelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /models/:id} : get the "id" model.
     *
     * @param id the id of the modelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the modelDTO, or with status {@code 404 (Not Found)}.
     * @throws Exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModelDTO> getModel(@PathVariable("id") Long id) throws Exception {
        log.debug("REST request to get Model : {}", id);
        RandomDelay.randomDelay(true);
        Optional<ModelDTO> modelDTO = modelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(modelDTO);
    }

    /**
     * {@code DELETE  /models/:id} : delete the "id" model.
     *
     * @param id the id of the modelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModel(@PathVariable("id") Long id) {
        log.debug("REST request to delete Model : {}", id);
        modelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
