package co.com.ies.pruebas.service;

import co.com.ies.pruebas.service.dto.SlotDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.ies.pruebas.domain.Slot}.
 */
public interface SlotService {
    /**
     * Save a slot.
     *
     * @param slotDTO the entity to save.
     * @return the persisted entity.
     */
    SlotDTO save(SlotDTO slotDTO);

    /**
     * Updates a slot.
     *
     * @param slotDTO the entity to update.
     * @return the persisted entity.
     */
    SlotDTO update(SlotDTO slotDTO);

    /**
     * Partially updates a slot.
     *
     * @param slotDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SlotDTO> partialUpdate(SlotDTO slotDTO);

    /**
     * Get all the slots with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SlotDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" slot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SlotDTO> findOne(Long id);

    /**
     * Delete the "id" slot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
