package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Entitya;
import com.mycompany.myapp.repository.EntityaRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Entitya}.
 */
@RestController
@RequestMapping("/api")
public class EntityaResource {

    private final Logger log = LoggerFactory.getLogger(EntityaResource.class);

    private static final String ENTITY_NAME = "serviceaEntitya";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntityaRepository entityaRepository;

    public EntityaResource(EntityaRepository entityaRepository) {
        this.entityaRepository = entityaRepository;
    }

    /**
     * {@code POST  /entityas} : Create a new entitya.
     *
     * @param entitya the entitya to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entitya, or with status {@code 400 (Bad Request)} if the entitya has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entityas")
    public ResponseEntity<Entitya> createEntitya(@RequestBody Entitya entitya) throws URISyntaxException {
        log.debug("REST request to save Entitya : {}", entitya);
        if (entitya.getId() != null) {
            throw new BadRequestAlertException("A new entitya cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entitya result = entityaRepository.save(entitya);
        return ResponseEntity.created(new URI("/api/entityas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entityas} : Updates an existing entitya.
     *
     * @param entitya the entitya to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entitya,
     * or with status {@code 400 (Bad Request)} if the entitya is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entitya couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entityas")
    public ResponseEntity<Entitya> updateEntitya(@RequestBody Entitya entitya) throws URISyntaxException {
        log.debug("REST request to update Entitya : {}", entitya);
        if (entitya.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Entitya result = entityaRepository.save(entitya);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, entitya.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /entityas} : get all the entityas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entityas in body.
     */
    @GetMapping("/entityas")
    public List<Entitya> getAllEntityas() {
        log.debug("REST request to get all Entityas");
        return entityaRepository.findAll();
    }

    /**
     * {@code GET  /entityas/:id} : get the "id" entitya.
     *
     * @param id the id of the entitya to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entitya, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entityas/{id}")
    public ResponseEntity<Entitya> getEntitya(@PathVariable Long id) {
        log.debug("REST request to get Entitya : {}", id);
        Optional<Entitya> entitya = entityaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entitya);
    }

    /**
     * {@code DELETE  /entityas/:id} : delete the "id" entitya.
     *
     * @param id the id of the entitya to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entityas/{id}")
    public ResponseEntity<Void> deleteEntitya(@PathVariable Long id) {
        log.debug("REST request to delete Entitya : {}", id);
        entityaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
