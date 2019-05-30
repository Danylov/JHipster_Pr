package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EntityB;
import com.mycompany.myapp.repository.EntityBRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.EntityB}.
 */
@RestController
@RequestMapping("/api")
public class EntityBResource {

    private final Logger log = LoggerFactory.getLogger(EntityBResource.class);

    private static final String ENTITY_NAME = "servicebEntityB";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntityBRepository entityBRepository;

    public EntityBResource(EntityBRepository entityBRepository) {
        this.entityBRepository = entityBRepository;
    }

    /**
     * {@code POST  /entity-bs} : Create a new entityB.
     *
     * @param entityB the entityB to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityB, or with status {@code 400 (Bad Request)} if the entityB has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entity-bs")
    public ResponseEntity<EntityB> createEntityB(@RequestBody EntityB entityB) throws URISyntaxException {
        log.debug("REST request to save EntityB : {}", entityB);
        if (entityB.getId() != null) {
            throw new BadRequestAlertException("A new entityB cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntityB result = entityBRepository.save(entityB);
        return ResponseEntity.created(new URI("/api/entity-bs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entity-bs} : Updates an existing entityB.
     *
     * @param entityB the entityB to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityB,
     * or with status {@code 400 (Bad Request)} if the entityB is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityB couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entity-bs")
    public ResponseEntity<EntityB> updateEntityB(@RequestBody EntityB entityB) throws URISyntaxException {
        log.debug("REST request to update EntityB : {}", entityB);
        if (entityB.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EntityB result = entityBRepository.save(entityB);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, entityB.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entity-bs} : get all the entityBS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entityBS in body.
     */
    @GetMapping("/entity-bs")
    public List<EntityB> getAllEntityBS() {
        log.debug("REST request to get all EntityBS");
        return entityBRepository.findAll();
    }

    /**
     * {@code GET  /entity-bs/:id} : get the "id" entityB.
     *
     * @param id the id of the entityB to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityB, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entity-bs/{id}")
    public ResponseEntity<EntityB> getEntityB(@PathVariable Long id) {
        log.debug("REST request to get EntityB : {}", id);
        Optional<EntityB> entityB = entityBRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entityB);
    }

    /**
     * {@code DELETE  /entity-bs/:id} : delete the "id" entityB.
     *
     * @param id the id of the entityB to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entity-bs/{id}")
    public ResponseEntity<Void> deleteEntityB(@PathVariable Long id) {
        log.debug("REST request to delete EntityB : {}", id);
        entityBRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
