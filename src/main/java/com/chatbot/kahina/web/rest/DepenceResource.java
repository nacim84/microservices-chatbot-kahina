package com.chatbot.kahina.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.chatbot.kahina.service.DepenceService;
import com.chatbot.kahina.web.rest.errors.BadRequestAlertException;
import com.chatbot.kahina.web.rest.util.HeaderUtil;
import com.chatbot.kahina.service.dto.DepenceDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Depence.
 */
@RestController
@RequestMapping("/api")
public class DepenceResource {

    private final Logger log = LoggerFactory.getLogger(DepenceResource.class);

    private static final String ENTITY_NAME = "depence";

    private final DepenceService depenceService;

    public DepenceResource(DepenceService depenceService) {
        this.depenceService = depenceService;
    }

    /**
     * POST  /depences : Create a new depence.
     *
     * @param depenceDTO the depenceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new depenceDTO, or with status 400 (Bad Request) if the depence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/depences")
    @Timed
    public ResponseEntity<DepenceDTO> createDepence(@Valid @RequestBody DepenceDTO depenceDTO) throws URISyntaxException {
        log.debug("REST request to save Depence : {}", depenceDTO);
        if (depenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new depence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepenceDTO result = depenceService.save(depenceDTO);
        return ResponseEntity.created(new URI("/api/depences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /depences : Updates an existing depence.
     *
     * @param depenceDTO the depenceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated depenceDTO,
     * or with status 400 (Bad Request) if the depenceDTO is not valid,
     * or with status 500 (Internal Server Error) if the depenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/depences")
    @Timed
    public ResponseEntity<DepenceDTO> updateDepence(@Valid @RequestBody DepenceDTO depenceDTO) throws URISyntaxException {
        log.debug("REST request to update Depence : {}", depenceDTO);
        if (depenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DepenceDTO result = depenceService.save(depenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, depenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /depences : get all the depences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of depences in body
     */
    @GetMapping("/depences")
    @Timed
    public List<DepenceDTO> getAllDepences() {
        log.debug("REST request to get all Depences");
        return depenceService.findAll();
    }

    /**
     * GET  /depences/:id : get the "id" depence.
     *
     * @param id the id of the depenceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the depenceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/depences/{id}")
    @Timed
    public ResponseEntity<DepenceDTO> getDepence(@PathVariable Long id) {
        log.debug("REST request to get Depence : {}", id);
        Optional<DepenceDTO> depenceDTO = depenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depenceDTO);
    }

    /**
     * DELETE  /depences/:id : delete the "id" depence.
     *
     * @param id the id of the depenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/depences/{id}")
    @Timed
    public ResponseEntity<Void> deleteDepence(@PathVariable Long id) {
        log.debug("REST request to delete Depence : {}", id);
        depenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
