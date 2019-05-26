package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServiceaApp;
import com.mycompany.myapp.config.SecurityBeanOverrideConfiguration;
import com.mycompany.myapp.domain.Entitya;
import com.mycompany.myapp.repository.EntityaRepository;
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
 * Integration tests for the {@Link EntityaResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, ServiceaApp.class})
public class EntityaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EntityaRepository entityaRepository;

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

    private MockMvc restEntityaMockMvc;

    private Entitya entitya;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntityaResource entityaResource = new EntityaResource(entityaRepository);
        this.restEntityaMockMvc = MockMvcBuilders.standaloneSetup(entityaResource)
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
    public static Entitya createEntity(EntityManager em) {
        Entitya entitya = new Entitya()
            .name(DEFAULT_NAME);
        return entitya;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entitya createUpdatedEntity(EntityManager em) {
        Entitya entitya = new Entitya()
            .name(UPDATED_NAME);
        return entitya;
    }

    @BeforeEach
    public void initTest() {
        entitya = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntitya() throws Exception {
        int databaseSizeBeforeCreate = entityaRepository.findAll().size();

        // Create the Entitya
        restEntityaMockMvc.perform(post("/api/entityas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entitya)))
            .andExpect(status().isCreated());

        // Validate the Entitya in the database
        List<Entitya> entityaList = entityaRepository.findAll();
        assertThat(entityaList).hasSize(databaseSizeBeforeCreate + 1);
        Entitya testEntitya = entityaList.get(entityaList.size() - 1);
        assertThat(testEntitya.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEntityaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityaRepository.findAll().size();

        // Create the Entitya with an existing ID
        entitya.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityaMockMvc.perform(post("/api/entityas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entitya)))
            .andExpect(status().isBadRequest());

        // Validate the Entitya in the database
        List<Entitya> entityaList = entityaRepository.findAll();
        assertThat(entityaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = entityaRepository.findAll().size();
        // set the field null
        entitya.setName(null);

        // Create the Entitya, which fails.

        restEntityaMockMvc.perform(post("/api/entityas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entitya)))
            .andExpect(status().isBadRequest());

        List<Entitya> entityaList = entityaRepository.findAll();
        assertThat(entityaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEntityas() throws Exception {
        // Initialize the database
        entityaRepository.saveAndFlush(entitya);

        // Get all the entityaList
        restEntityaMockMvc.perform(get("/api/entityas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entitya.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getEntitya() throws Exception {
        // Initialize the database
        entityaRepository.saveAndFlush(entitya);

        // Get the entitya
        restEntityaMockMvc.perform(get("/api/entityas/{id}", entitya.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entitya.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEntitya() throws Exception {
        // Get the entitya
        restEntityaMockMvc.perform(get("/api/entityas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntitya() throws Exception {
        // Initialize the database
        entityaRepository.saveAndFlush(entitya);

        int databaseSizeBeforeUpdate = entityaRepository.findAll().size();

        // Update the entitya
        Entitya updatedEntitya = entityaRepository.findById(entitya.getId()).get();
        // Disconnect from session so that the updates on updatedEntitya are not directly saved in db
        em.detach(updatedEntitya);
        updatedEntitya
            .name(UPDATED_NAME);

        restEntityaMockMvc.perform(put("/api/entityas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntitya)))
            .andExpect(status().isOk());

        // Validate the Entitya in the database
        List<Entitya> entityaList = entityaRepository.findAll();
        assertThat(entityaList).hasSize(databaseSizeBeforeUpdate);
        Entitya testEntitya = entityaList.get(entityaList.size() - 1);
        assertThat(testEntitya.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEntitya() throws Exception {
        int databaseSizeBeforeUpdate = entityaRepository.findAll().size();

        // Create the Entitya

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntityaMockMvc.perform(put("/api/entityas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entitya)))
            .andExpect(status().isBadRequest());

        // Validate the Entitya in the database
        List<Entitya> entityaList = entityaRepository.findAll();
        assertThat(entityaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEntitya() throws Exception {
        // Initialize the database
        entityaRepository.saveAndFlush(entitya);

        int databaseSizeBeforeDelete = entityaRepository.findAll().size();

        // Delete the entitya
        restEntityaMockMvc.perform(delete("/api/entityas/{id}", entitya.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Entitya> entityaList = entityaRepository.findAll();
        assertThat(entityaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entitya.class);
        Entitya entitya1 = new Entitya();
        entitya1.setId(1L);
        Entitya entitya2 = new Entitya();
        entitya2.setId(entitya1.getId());
        assertThat(entitya1).isEqualTo(entitya2);
        entitya2.setId(2L);
        assertThat(entitya1).isNotEqualTo(entitya2);
        entitya1.setId(null);
        assertThat(entitya1).isNotEqualTo(entitya2);
    }
}
