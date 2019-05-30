package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServicebApp;
import com.mycompany.myapp.config.SecurityBeanOverrideConfiguration;
import com.mycompany.myapp.domain.EntityB;
import com.mycompany.myapp.repository.EntityBRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link EntityBResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, ServicebApp.class})
public class EntityBResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EntityBRepository entityBRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEntityBMockMvc;

    private EntityB entityB;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntityBResource entityBResource = new EntityBResource(entityBRepository);
        this.restEntityBMockMvc = MockMvcBuilders.standaloneSetup(entityBResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityB createEntity(EntityManager em) {
        EntityB entityB = new EntityB()
            .name(DEFAULT_NAME);
        return entityB;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityB createUpdatedEntity(EntityManager em) {
        EntityB entityB = new EntityB()
            .name(UPDATED_NAME);
        return entityB;
    }

    @BeforeEach
    public void initTest() {
        entityB = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityB() throws Exception {
        int databaseSizeBeforeCreate = entityBRepository.findAll().size();

        // Create the EntityB
        restEntityBMockMvc.perform(post("/api/entity-bs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityB)))
            .andExpect(status().isCreated());

        // Validate the EntityB in the database
        List<EntityB> entityBList = entityBRepository.findAll();
        assertThat(entityBList).hasSize(databaseSizeBeforeCreate + 1);
        EntityB testEntityB = entityBList.get(entityBList.size() - 1);
        assertThat(testEntityB.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEntityBWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityBRepository.findAll().size();

        // Create the EntityB with an existing ID
        entityB.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityBMockMvc.perform(post("/api/entity-bs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityB)))
            .andExpect(status().isBadRequest());

        // Validate the EntityB in the database
        List<EntityB> entityBList = entityBRepository.findAll();
        assertThat(entityBList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEntityBS() throws Exception {
        // Initialize the database
        entityBRepository.saveAndFlush(entityB);

        // Get all the entityBList
        restEntityBMockMvc.perform(get("/api/entity-bs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityB.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getEntityB() throws Exception {
        // Initialize the database
        entityBRepository.saveAndFlush(entityB);

        // Get the entityB
        restEntityBMockMvc.perform(get("/api/entity-bs/{id}", entityB.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityB.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEntityB() throws Exception {
        // Get the entityB
        restEntityBMockMvc.perform(get("/api/entity-bs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityB() throws Exception {
        // Initialize the database
        entityBRepository.saveAndFlush(entityB);

        int databaseSizeBeforeUpdate = entityBRepository.findAll().size();

        // Update the entityB
        EntityB updatedEntityB = entityBRepository.findById(entityB.getId()).get();
        // Disconnect from session so that the updates on updatedEntityB are not directly saved in db
        em.detach(updatedEntityB);
        updatedEntityB
            .name(UPDATED_NAME);

        restEntityBMockMvc.perform(put("/api/entity-bs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityB)))
            .andExpect(status().isOk());

        // Validate the EntityB in the database
        List<EntityB> entityBList = entityBRepository.findAll();
        assertThat(entityBList).hasSize(databaseSizeBeforeUpdate);
        EntityB testEntityB = entityBList.get(entityBList.size() - 1);
        assertThat(testEntityB.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityB() throws Exception {
        int databaseSizeBeforeUpdate = entityBRepository.findAll().size();

        // Create the EntityB

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntityBMockMvc.perform(put("/api/entity-bs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityB)))
            .andExpect(status().isBadRequest());

        // Validate the EntityB in the database
        List<EntityB> entityBList = entityBRepository.findAll();
        assertThat(entityBList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEntityB() throws Exception {
        // Initialize the database
        entityBRepository.saveAndFlush(entityB);

        int databaseSizeBeforeDelete = entityBRepository.findAll().size();

        // Delete the entityB
        restEntityBMockMvc.perform(delete("/api/entity-bs/{id}", entityB.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<EntityB> entityBList = entityBRepository.findAll();
        assertThat(entityBList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityB.class);
        EntityB entityB1 = new EntityB();
        entityB1.setId(1L);
        EntityB entityB2 = new EntityB();
        entityB2.setId(entityB1.getId());
        assertThat(entityB1).isEqualTo(entityB2);
        entityB2.setId(2L);
        assertThat(entityB1).isNotEqualTo(entityB2);
        entityB1.setId(null);
        assertThat(entityB1).isNotEqualTo(entityB2);
    }
}
