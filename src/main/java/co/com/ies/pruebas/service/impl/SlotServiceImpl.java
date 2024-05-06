package co.com.ies.pruebas.service.impl;

import co.com.ies.pruebas.domain.Slot;
import co.com.ies.pruebas.repository.SlotRepository;
import co.com.ies.pruebas.service.SlotService;
import co.com.ies.pruebas.service.dto.SlotDTO;
import co.com.ies.pruebas.service.mapper.SlotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.com.ies.pruebas.domain.Slot}.
 */
@Service
@Transactional
public class SlotServiceImpl implements SlotService {

    private final Logger log = LoggerFactory.getLogger(SlotServiceImpl.class);

    private final SlotRepository slotRepository;

    private final SlotMapper slotMapper;

    public SlotServiceImpl(SlotRepository slotRepository, SlotMapper slotMapper) {
        this.slotRepository = slotRepository;
        this.slotMapper = slotMapper;
    }

    @Override
    public SlotDTO save(SlotDTO slotDTO) {
        log.debug("Request to save Slot : {}", slotDTO);
        Slot slot = slotMapper.toEntity(slotDTO);
        slot = slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    @Override
    public SlotDTO update(SlotDTO slotDTO) {
        log.debug("Request to update Slot : {}", slotDTO);
        Slot slot = slotMapper.toEntity(slotDTO);
        slot = slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    @Override
    public Optional<SlotDTO> partialUpdate(SlotDTO slotDTO) {
        log.debug("Request to partially update Slot : {}", slotDTO);

        return slotRepository
            .findById(slotDTO.getId())
            .map(existingSlot -> {
                slotMapper.partialUpdate(existingSlot, slotDTO);

                return existingSlot;
            })
            .map(slotRepository::save)
            .map(slotMapper::toDto);
    }

    public Page<SlotDTO> findAllWithEagerRelationships(Pageable pageable) {
        return slotRepository.findAllWithEagerRelationships(pageable).map(slotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SlotDTO> findOne(Long id) {
        log.debug("Request to get Slot : {}", id);
        return slotRepository.findOneWithEagerRelationships(id).map(slotMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Slot : {}", id);
        slotRepository.deleteById(id);
    }
}
