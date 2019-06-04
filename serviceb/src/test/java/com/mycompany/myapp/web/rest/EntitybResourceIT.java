package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.ServicebApp;
import com.mycompany.myapp.config.SecurityBeanOverrideConfiguration;
import com.mycompany.myapp.domain.Entityb;
import com.mycompany.myapp.repository.EntitybRepository;
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
 * Integration tests for the {@Link EntitybResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, ServicebApp.class})
public class EntitybResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EntitybRepository entitybRepository;

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

    private MockMvc restEntitybMockMvc;

    private Entityb entityb;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntitybResource entitybResource = new EntitybResource(entitybRepository);
        this.restEntitybMockMvc = MockMvcBuilders.standaloneSetup(entitybResource)
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
    public static Entityb createEntity(EntityManager em) {
        Entityb entityb = new Entityb()
            .name(DEFAULT_NAME);
        return entityb;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entityb createUpdatedEntity(EntityManager em) {
        Entityb entityb = new Entityb()
            .name(UPDATED_NAME);
        return entityb;
    }

    @BeforeEach
    public void initTest() {
        entityb = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityb() throws Exception {
        int databaseSizeBeforeCreate = entitybRepository.findAll().size();

        // Create the Entityb
        restEntitybMockMvc.perform(post("/api/entitybs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityb)))
            .andExpect(status().isCreated());

        // Validate the Entityb in the database
        List<Entityb> entitybList = entitybRepository.findAll();
        assertThat(entitybList).hasSize(databaseSizeBeforeCreate + 1);
        Entityb testEntityb = entitybList.get(entitybList.size() - 1);
        assertThat(testEntityb.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEntitybWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entitybRepository.findAll().size();

        // Create the Entityb with an existing ID
        entityb.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntitybMockMvc.perform(post("/api/entitybs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityb)))
            .andExpect(status().isBadRequest());

        // Validate the Entityb in the database
        List<Entityb> entitybList = entitybRepository.findAll();
        assertThat(entitybList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEntitybs() throws Exception {
        // Initialize the database
        entitybRepository.saveAndFlush(entityb);

        // Get all the entitybList
        restEntitybMockMvc.perform(get("/api/entitybs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityb.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getEntityb() throws Exception {
        // Initialize the database
        entitybRepository.saveAndFlush(entityb);

        // Get the entityb
        restEntitybMockMvc.perform(get("/api/entitybs/{id}", entityb.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityb.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEntityb() throws Exception {
        // Get the entityb
        restEntitybMockMvc.perform(get("/api/entitybs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityb() throws Exception {
        // Initialize the database
        entitybRepository.saveAndFlush(entityb);

        int databaseSizeBeforeUpdate = entitybRepository.findAll().size();

        // Update the entityb
        Entityb updatedEntityb = entitybRepository.findById(entityb.getId()).get();
        // Disconnect from session so that the updates on updatedEntityb are not directly saved in db
        em.detach(updatedEntityb);
        updatedEntityb
            .name(UPDATED_NAME);

        restEntitybMockMvc.perform(put("/api/entitybs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityb)))
            .andExpect(status().isOk());

        // Validate the Entityb in the database
        List<Entityb> entitybList = entitybRepository.findAll();
        assertThat(entitybList).hasSize(databaseSizeBeforeUpdate);
        Entityb testEntityb = entitybList.get(entitybList.size() - 1);
        assertThat(testEntityb.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityb() throws Exception {
        int databaseSizeBeforeUpdate = entitybRepository.findAll().size();

        // Create the Entityb

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntitybMockMvc.perform(put("/api/entitybs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityb)))
            .andExpect(status().isBadRequest());

        // Validate the Entityb in the database
        List<Entityb> entitybList = entitybRepository.findAll();
        assertThat(entitybList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEntityb() throws Exception {
        // Initialize the database
        entitybRepository.saveAndFlush(entityb);

        int databaseSizeBeforeDelete = entitybRepository.findAll().size();

        // Delete the entityb
        restEntitybMockMvc.perform(delete("/api/entitybs/{id}", entityb.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Entityb> entitybList = entitybRepository.findAll();
        assertThat(entitybList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entityb.class);
        Entityb entityb1 = new Entityb();
        entityb1.setId(1L);
        Entityb entityb2 = new Entityb();
        entityb2.setId(entityb1.getId());
        assertThat(entityb1).isEqualTo(entityb2);
        entityb2.setId(2L);
        assertThat(entityb1).isNotEqualTo(entityb2);
        entityb1.setId(null);
        assertThat(entityb1).isNotEqualTo(entityb2);
    }
}
