package co.com.ies.pruebas.service;

import co.com.ies.pruebas.service.dto.ModelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.pruebas.domain.Model}.
 */
public interface ModelService {
    /**
     * Save a model.
     *
     * @param modelDTO the entity to save.
     * @return the persisted entity.
     */
    ModelDTO save(ModelDTO modelDTO);

    /**
     * Updates a model.
     *
     * @param modelDTO the entity to update.
     * @return the persisted entity.
     */
    ModelDTO update(ModelDTO modelDTO);

    /**
     * Partially updates a model.
     *
     * @param modelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ModelDTO> partialUpdate(ModelDTO modelDTO);

    /**
     * Get all the models with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ModelDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" model.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ModelDTO> findOne(Long id);

    /**
     * Delete the "id" model.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
