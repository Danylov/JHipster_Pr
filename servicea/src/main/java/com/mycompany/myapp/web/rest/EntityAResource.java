package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EntityA;
import com.mycompany.myapp.repository.EntityARepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EntityA}.
 */
@RestController
@RequestMapping("/api")
public class EntityAResource {

    private final Logger log = LoggerFactory.getLogger(EntityAResource.class);

    private static final String ENTITY_NAME = "serviceaEntityA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntityARepository entityARepository;

    public EntityAResource(EntityARepository entityARepository) {
        this.entityARepository = entityARepository;
    }

    /**
     * {@code POST  /entity-as} : Create a new entityA.
     *
     * @param entityA the entityA to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityA, or with status {@code 400 (Bad Request)} if the entityA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entity-as")
    public ResponseEntity<EntityA> createEntityA(@RequestBody EntityA entityA) throws URISyntaxException {
        log.debug("REST request to save EntityA : {}", entityA);
        if (entityA.getId() != null) {
            throw new BadRequestAlertException("A new entityA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntityA result = entityARepository.save(entityA);
        return ResponseEntity.created(new URI("/api/entity-as/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entity-as} : Updates an existing entityA.
     *
     * @param entityA the entityA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityA,
     * or with status {@code 400 (Bad Request)} if the entityA is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entity-as")
    public ResponseEntity<EntityA> updateEntityA(@RequestBody EntityA entityA) throws URISyntaxException {
        log.debug("REST request to update EntityA : {}", entityA);
        if (entityA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EntityA result = entityARepository.save(entityA);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, entityA.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entity-as} : get all the entityAS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entityAS in body.
     */
    @GetMapping("/entity-as")
    public List<EntityA> getAllEntityAS() {
        log.debug("REST request to get all EntityAS");
        return entityARepository.findAll();
    }

    /**
     * {@code GET  /entity-as/:id} : get the "id" entityA.
     *
     * @param id the id of the entityA to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityA, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entity-as/{id}")
    public ResponseEntity<EntityA> getEntityA(@PathVariable Long id) {
        log.debug("REST request to get EntityA : {}", id);
        Optional<EntityA> entityA = entityARepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entityA);
    }

    /**
     * {@code DELETE  /entity-as/:id} : delete the "id" entityA.
     *
     * @param id the id of the entityA to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entity-as/{id}")
    public ResponseEntity<Void> deleteEntityA(@PathVariable Long id) {
        log.debug("REST request to delete EntityA : {}", id);
        entityARepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
