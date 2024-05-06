package co.com.ies.pruebas.service;

import co.com.ies.pruebas.service.dto.ManufacturerDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link co.com.ies.pruebas.domain.Manufacturer}.
 */
public interface ManufacturerService {
    /**
     * Save a manufacturer.
     *
     * @param manufacturerDTO the entity to save.
     * @return the persisted entity.
     */
    ManufacturerDTO save(ManufacturerDTO manufacturerDTO);

    /**
     * Updates a manufacturer.
     *
     * @param manufacturerDTO the entity to update.
     * @return the persisted entity.
     */
    ManufacturerDTO update(ManufacturerDTO manufacturerDTO);

    /**
     * Partially updates a manufacturer.
     *
     * @param manufacturerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ManufacturerDTO> partialUpdate(ManufacturerDTO manufacturerDTO);

    /**
     * Get the "id" manufacturer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ManufacturerDTO> findOne(Long id);

    /**
     * Delete the "id" manufacturer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
