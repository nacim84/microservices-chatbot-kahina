package com.chatbot.kahina.service;

import com.chatbot.kahina.service.dto.DepenceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Depence.
 */
public interface DepenceService {

    /**
     * Save a depence.
     *
     * @param depenceDTO the entity to save
     * @return the persisted entity
     */
    DepenceDTO save(DepenceDTO depenceDTO);

    /**
     * Get all the depences.
     *
     * @return the list of entities
     */
    List<DepenceDTO> findAll();


    /**
     * Get the "id" depence.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<DepenceDTO> findOne(Long id);

    /**
     * Delete the "id" depence.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
