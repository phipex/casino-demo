package co.com.ies.pruebas.service;

import co.com.ies.pruebas.domain.*; // for static metamodels
import co.com.ies.pruebas.domain.Casino;
import co.com.ies.pruebas.repository.CasinoRepository;
import co.com.ies.pruebas.service.criteria.CasinoCriteria;
import co.com.ies.pruebas.service.dto.CasinoDTO;
import co.com.ies.pruebas.service.mapper.CasinoMapper;
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
 * Service for executing complex queries for {@link Casino} entities in the database.
 * The main input is a {@link CasinoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CasinoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CasinoQueryService extends QueryService<Casino> {

    private final Logger log = LoggerFactory.getLogger(CasinoQueryService.class);

    private final CasinoRepository casinoRepository;

    private final CasinoMapper casinoMapper;

    public CasinoQueryService(CasinoRepository casinoRepository, CasinoMapper casinoMapper) {
        this.casinoRepository = casinoRepository;
        this.casinoMapper = casinoMapper;
    }

    /**
     * Return a {@link Page} of {@link CasinoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CasinoDTO> findByCriteria(CasinoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Casino> specification = createSpecification(criteria);
        return casinoRepository.findAll(specification, page).map(casinoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CasinoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Casino> specification = createSpecification(criteria);
        return casinoRepository.count(specification);
    }

    /**
     * Function to convert {@link CasinoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Casino> createSpecification(CasinoCriteria criteria) {
        Specification<Casino> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Casino_.id));
            }
            if (criteria.getNit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNit(), Casino_.nit));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Casino_.name));
            }
            if (criteria.getDirection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDirection(), Casino_.direction));
            }
            if (criteria.getOperatorId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOperatorId(), root -> root.join(Casino_.operator, JoinType.LEFT).get(Operator_.id))
                );
            }
        }
        return specification;
    }
}
