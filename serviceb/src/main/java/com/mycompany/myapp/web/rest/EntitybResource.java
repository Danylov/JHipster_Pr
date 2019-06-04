package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Entityb;
import com.mycompany.myapp.repository.EntitybRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Entityb}.
 */
@RestController
@RequestMapping("/api")
public class EntitybResource {

    private final Logger log = LoggerFactory.getLogger(EntitybResource.class);

    private static final String ENTITY_NAME = "servicebEntityb";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntitybRepository entitybRepository;

    public EntitybResource(EntitybRepository entitybRepository) {
        this.entitybRepository = entitybRepository;
    }

    /**
     * {@code POST  /entitybs} : Create a new entityb.
     *
     * @param entityb the entityb to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entityb, or with status {@code 400 (Bad Request)} if the entityb has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entitybs")
    public ResponseEntity<Entityb> createEntityb(@RequestBody Entityb entityb) throws URISyntaxException {
        log.debug("REST request to save Entityb : {}", entityb);
        if (entityb.getId() != null) {
            throw new BadRequestAlertException("A new entityb cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entityb result = entitybRepository.save(entityb);
        return ResponseEntity.created(new URI("/api/entitybs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entitybs} : Updates an existing entityb.
     *
     * @param entityb the entityb to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entityb,
     * or with status {@code 400 (Bad Request)} if the entityb is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entityb couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entitybs")
    public ResponseEntity<Entityb> updateEntityb(@RequestBody Entityb entityb) throws URISyntaxException {
        log.debug("REST request to update Entityb : {}", entityb);
        if (entityb.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Entityb result = entitybRepository.save(entityb);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, entityb.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entitybs} : get all the entitybs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entitybs in body.
     */
    @GetMapping("/entitybs")
    public List<Entityb> getAllEntitybs() {
        log.debug("REST request to get all Entitybs");
        return entitybRepository.findAll();
    }

    /**
     * {@code GET  /entitybs/:id} : get the "id" entityb.
     *
     * @param id the id of the entityb to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entityb, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entitybs/{id}")
    public ResponseEntity<Entityb> getEntityb(@PathVariable Long id) {
        log.debug("REST request to get Entityb : {}", id);
        Optional<Entityb> entityb = entitybRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entityb);
    }

    /**
     * {@code DELETE  /entitybs/:id} : delete the "id" entityb.
     *
     * @param id the id of the entityb to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entitybs/{id}")
    public ResponseEntity<Void> deleteEntityb(@PathVariable Long id) {
        log.debug("REST request to delete Entityb : {}", id);
        entitybRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
