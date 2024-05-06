package co.com.ies.pruebas.service;

import co.com.ies.pruebas.domain.*; // for static metamodels
import co.com.ies.pruebas.domain.Slot;
import co.com.ies.pruebas.repository.SlotRepository;
import co.com.ies.pruebas.service.criteria.SlotCriteria;
import co.com.ies.pruebas.service.dto.SlotDTO;
import co.com.ies.pruebas.service.mapper.SlotMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Slot} entities in the database.
 * The main input is a {@link SlotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SlotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SlotQueryService extends QueryService<Slot> {

    private final Logger log = LoggerFactory.getLogger(SlotQueryService.class);

    private final SlotRepository slotRepository;

    private final SlotMapper slotMapper;

    public SlotQueryService(SlotRepository slotRepository, SlotMapper slotMapper) {
        this.slotRepository = slotRepository;
        this.slotMapper = slotMapper;
    }

    /**
     * Return a {@link Page} of {@link SlotDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SlotDTO> findByCriteria(SlotCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Slot> specification = createSpecification(criteria);
        return slotRepository.findAll(specification, page).map(slotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SlotCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Slot> specification = createSpecification(criteria);
        return slotRepository.count(specification);
    }

    /**
     * Function to convert {@link SlotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Slot> createSpecification(SlotCriteria criteria) {
        Specification<Slot> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Slot_.id));
            }
            if (criteria.getIdCasino() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdCasino(), Slot_.idCasino));
            }
            if (criteria.getSerial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerial(), Slot_.serial));
            }
            if (criteria.getNuc() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNuc(), Slot_.nuc));
            }
            if (criteria.getInitialized() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInitialized(), Slot_.initialized));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), Slot_.balance));
            }
            if (criteria.getCasinoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCasinoId(), root -> root.join(Slot_.casino, JoinType.LEFT).get(Casino_.id))
                );
            }
            if (criteria.getModelId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getModelId(), root -> root.join(Slot_.model, JoinType.LEFT).get(Model_.id))
                );
            }
        }
        return specification;
    }
}
