package co.com.ies.pruebas.web.rest;

import co.com.ies.pruebas.repository.SlotRepository;
import co.com.ies.pruebas.service.SlotQueryService;
import co.com.ies.pruebas.service.SlotService;
import co.com.ies.pruebas.service.criteria.SlotCriteria;
import co.com.ies.pruebas.service.dto.SlotDTO;
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
 * REST controller for managing {@link co.com.ies.pruebas.domain.Slot}.
 */
@RestController
@RequestMapping("/api/slots")
public class SlotResource {

    private final Logger log = LoggerFactory.getLogger(SlotResource.class);

    private static final String ENTITY_NAME = "slot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SlotService slotService;

    private final SlotRepository slotRepository;

    private final SlotQueryService slotQueryService;

    public SlotResource(SlotService slotService, SlotRepository slotRepository, SlotQueryService slotQueryService) {
        this.slotService = slotService;
        this.slotRepository = slotRepository;
        this.slotQueryService = slotQueryService;
    }

    /**
     * {@code POST  /slots} : Create a new slot.
     *
     * @param slotDTO the slotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new slotDTO, or with status {@code 400 (Bad Request)} if the slot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SlotDTO> createSlot(@Valid @RequestBody SlotDTO slotDTO) throws URISyntaxException {
        log.debug("REST request to save Slot : {}", slotDTO);
        if (slotDTO.getId() != null) {
            throw new BadRequestAlertException("A new slot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        slotDTO = slotService.save(slotDTO);
        return ResponseEntity.created(new URI("/api/slots/" + slotDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, slotDTO.getId().toString()))
            .body(slotDTO);
    }

    /**
     * {@code PUT  /slots/:id} : Updates an existing slot.
     *
     * @param id the id of the slotDTO to save.
     * @param slotDTO the slotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slotDTO,
     * or with status {@code 400 (Bad Request)} if the slotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the slotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SlotDTO> updateSlot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SlotDTO slotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Slot : {}, {}", id, slotDTO);
        if (slotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        slotDTO = slotService.update(slotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slotDTO.getId().toString()))
            .body(slotDTO);
    }

    /**
     * {@code PATCH  /slots/:id} : Partial updates given fields of an existing slot, field will ignore if it is null
     *
     * @param id the id of the slotDTO to save.
     * @param slotDTO the slotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated slotDTO,
     * or with status {@code 400 (Bad Request)} if the slotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the slotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the slotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SlotDTO> partialUpdateSlot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SlotDTO slotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Slot partially : {}, {}", id, slotDTO);
        if (slotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, slotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!slotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SlotDTO> result = slotService.partialUpdate(slotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, slotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /slots} : get all the slots.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of slots in body.
     * @throws Exception
     */
    @GetMapping("")
    public ResponseEntity<List<SlotDTO>> getAllSlots(
        SlotCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) throws Exception {
        log.debug("REST request to get Slots by criteria: {}", criteria);
        RandomDelay.randomDelay(true);
        Page<SlotDTO> page = slotQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /slots/count} : count all the slots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSlots(SlotCriteria criteria) {
        log.debug("REST request to count Slots by criteria: {}", criteria);
        return ResponseEntity.ok().body(slotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /slots/:id} : get the "id" slot.
     *
     * @param id the id of the slotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the slotDTO, or with status {@code 404 (Not Found)}.
     * @throws Exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<SlotDTO> getSlot(@PathVariable("id") Long id) throws Exception {
        log.debug("REST request to get Slot : {}", id);
        RandomDelay.randomDelay(true);
        Optional<SlotDTO> slotDTO = slotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(slotDTO);
    }

    /**
     * {@code DELETE  /slots/:id} : delete the "id" slot.
     *
     * @param id the id of the slotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable("id") Long id) {
        log.debug("REST request to delete Slot : {}", id);
        slotService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
