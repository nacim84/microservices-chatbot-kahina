package com.chatbot.kahina.service.impl;

import com.chatbot.kahina.service.DepenceService;
import com.chatbot.kahina.domain.Depence;
import com.chatbot.kahina.repository.DepenceRepository;
import com.chatbot.kahina.service.dto.DepenceDTO;
import com.chatbot.kahina.service.mapper.DepenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing Depence.
 */
@Service
@Transactional
public class DepenceServiceImpl implements DepenceService {

    private final Logger log = LoggerFactory.getLogger(DepenceServiceImpl.class);

    private final DepenceRepository depenceRepository;

    private final DepenceMapper depenceMapper;

    public DepenceServiceImpl(DepenceRepository depenceRepository, DepenceMapper depenceMapper) {
        this.depenceRepository = depenceRepository;
        this.depenceMapper = depenceMapper;
    }

    /**
     * Save a depence.
     *
     * @param depenceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DepenceDTO save(DepenceDTO depenceDTO) {
        log.debug("Request to save Depence : {}", depenceDTO);
        Depence depence = depenceMapper.toEntity(depenceDTO);
        depence = depenceRepository.save(depence);
        return depenceMapper.toDto(depence);
    }

    /**
     * Get all the depences.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepenceDTO> findAll() {
        log.debug("Request to get all Depences");
        return depenceRepository.findAll().stream()
            .map(depenceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one depence by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DepenceDTO> findOne(Long id) {
        log.debug("Request to get Depence : {}", id);
        return depenceRepository.findById(id)
            .map(depenceMapper::toDto);
    }

    /**
     * Delete the depence by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Depence : {}", id);
        depenceRepository.deleteById(id);
    }
}
