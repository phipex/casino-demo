package co.com.ies.pruebas.service.impl;

import co.com.ies.pruebas.domain.Casino;
import co.com.ies.pruebas.repository.CasinoRepository;
import co.com.ies.pruebas.service.CasinoService;
import co.com.ies.pruebas.service.dto.CasinoDTO;
import co.com.ies.pruebas.service.mapper.CasinoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link co.com.ies.pruebas.domain.Casino}.
 */
@Service
@Transactional
public class CasinoServiceImpl implements CasinoService {

    private final Logger log = LoggerFactory.getLogger(CasinoServiceImpl.class);

    private final CasinoRepository casinoRepository;

    private final CasinoMapper casinoMapper;

    public CasinoServiceImpl(CasinoRepository casinoRepository, CasinoMapper casinoMapper) {
        this.casinoRepository = casinoRepository;
        this.casinoMapper = casinoMapper;
    }

    @Override
    public CasinoDTO save(CasinoDTO casinoDTO) {
        log.debug("Request to save Casino : {}", casinoDTO);
        Casino casino = casinoMapper.toEntity(casinoDTO);
        casino = casinoRepository.save(casino);
        return casinoMapper.toDto(casino);
    }

    @Override
    public CasinoDTO update(CasinoDTO casinoDTO) {
        log.debug("Request to update Casino : {}", casinoDTO);
        Casino casino = casinoMapper.toEntity(casinoDTO);
        casino = casinoRepository.save(casino);
        return casinoMapper.toDto(casino);
    }

    @Override
    public Optional<CasinoDTO> partialUpdate(CasinoDTO casinoDTO) {
        log.debug("Request to partially update Casino : {}", casinoDTO);

        return casinoRepository
            .findById(casinoDTO.getId())
            .map(existingCasino -> {
                casinoMapper.partialUpdate(existingCasino, casinoDTO);

                return existingCasino;
            })
            .map(casinoRepository::save)
            .map(casinoMapper::toDto);
    }

    public Page<CasinoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return casinoRepository.findAllWithEagerRelationships(pageable).map(casinoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CasinoDTO> findOne(Long id) {
        log.debug("Request to get Casino : {}", id);
        return casinoRepository.findOneWithEagerRelationships(id).map(casinoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Casino : {}", id);
        casinoRepository.deleteById(id);
    }
}
