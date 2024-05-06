package co.com.ies.pruebas.service;

import co.com.ies.pruebas.service.dto.CasinoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.pruebas.domain.Casino}.
 */
public interface CasinoService {
    /**
     * Save a casino.
     *
     * @param casinoDTO the entity to save.
     * @return the persisted entity.
     */
    CasinoDTO save(CasinoDTO casinoDTO);

    /**
     * Updates a casino.
     *
     * @param casinoDTO the entity to update.
     * @return the persisted entity.
     */
    CasinoDTO update(CasinoDTO casinoDTO);

    /**
     * Partially updates a casino.
     *
     * @param casinoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CasinoDTO> partialUpdate(CasinoDTO casinoDTO);

    /**
     * Get all the casinos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CasinoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" casino.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CasinoDTO> findOne(Long id);

    /**
     * Delete the "id" casino.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
