package co.com.ies.pruebas.web.rest;

import co.com.ies.pruebas.repository.CasinoRepository;
import co.com.ies.pruebas.service.CasinoQueryService;
import co.com.ies.pruebas.service.CasinoService;
import co.com.ies.pruebas.service.criteria.CasinoCriteria;
import co.com.ies.pruebas.service.dto.CasinoDTO;
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
 * REST controller for managing {@link co.com.ies.pruebas.domain.Casino}.
 */
@RestController
@RequestMapping("/api/casinos")
public class CasinoResource {

    private final Logger log = LoggerFactory.getLogger(CasinoResource.class);

    private static final String ENTITY_NAME = "casino";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CasinoService casinoService;

    private final CasinoRepository casinoRepository;

    private final CasinoQueryService casinoQueryService;

    public CasinoResource(CasinoService casinoService, CasinoRepository casinoRepository, CasinoQueryService casinoQueryService) {
        this.casinoService = casinoService;
        this.casinoRepository = casinoRepository;
        this.casinoQueryService = casinoQueryService;
    }

    /**
     * {@code POST  /casinos} : Create a new casino.
     *
     * @param casinoDTO the casinoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new casinoDTO, or with status {@code 400 (Bad Request)} if the casino has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CasinoDTO> createCasino(@Valid @RequestBody CasinoDTO casinoDTO) throws URISyntaxException {
        log.debug("REST request to save Casino : {}", casinoDTO);
        if (casinoDTO.getId() != null) {
            throw new BadRequestAlertException("A new casino cannot already have an ID", ENTITY_NAME, "idexists");
        }
        casinoDTO = casinoService.save(casinoDTO);
        return ResponseEntity.created(new URI("/api/casinos/" + casinoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, casinoDTO.getId().toString()))
            .body(casinoDTO);
    }

    /**
     * {@code PUT  /casinos/:id} : Updates an existing casino.
     *
     * @param id the id of the casinoDTO to save.
     * @param casinoDTO the casinoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casinoDTO,
     * or with status {@code 400 (Bad Request)} if the casinoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the casinoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CasinoDTO> updateCasino(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CasinoDTO casinoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Casino : {}, {}", id, casinoDTO);
        if (casinoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casinoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casinoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        casinoDTO = casinoService.update(casinoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casinoDTO.getId().toString()))
            .body(casinoDTO);
    }

    /**
     * {@code PATCH  /casinos/:id} : Partial updates given fields of an existing casino, field will ignore if it is null
     *
     * @param id the id of the casinoDTO to save.
     * @param casinoDTO the casinoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated casinoDTO,
     * or with status {@code 400 (Bad Request)} if the casinoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the casinoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the casinoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CasinoDTO> partialUpdateCasino(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CasinoDTO casinoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Casino partially : {}, {}", id, casinoDTO);
        if (casinoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, casinoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!casinoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CasinoDTO> result = casinoService.partialUpdate(casinoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, casinoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /casinos} : get all the casinos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of casinos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CasinoDTO>> getAllCasinos(
        CasinoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Casinos by criteria: {}", criteria);
        RandomDelay.randomDelay();
        Page<CasinoDTO> page = casinoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /casinos/count} : count all the casinos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCasinos(CasinoCriteria criteria) {
        log.debug("REST request to count Casinos by criteria: {}", criteria);
        return ResponseEntity.ok().body(casinoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /casinos/:id} : get the "id" casino.
     *
     * @param id the id of the casinoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the casinoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CasinoDTO> getCasino(@PathVariable("id") Long id) {
        log.debug("REST request to get Casino : {}", id);
        RandomDelay.randomDelay();
        Optional<CasinoDTO> casinoDTO = casinoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(casinoDTO);
    }

    /**
     * {@code DELETE  /casinos/:id} : delete the "id" casino.
     *
     * @param id the id of the casinoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCasino(@PathVariable("id") Long id) {
        log.debug("REST request to delete Casino : {}", id);
        casinoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
