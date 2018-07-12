package com.chatbot.kahina.web.rest;

import com.chatbot.kahina.MicroserviceschatbotkahinaApp;

import com.chatbot.kahina.domain.Depence;
import com.chatbot.kahina.repository.DepenceRepository;
import com.chatbot.kahina.service.DepenceService;
import com.chatbot.kahina.service.dto.DepenceDTO;
import com.chatbot.kahina.service.mapper.DepenceMapper;
import com.chatbot.kahina.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static com.chatbot.kahina.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DepenceResource REST controller.
 *
 * @see DepenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicroserviceschatbotkahinaApp.class)
public class DepenceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MONTANT = 1;
    private static final Integer UPDATED_MONTANT = 2;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private DepenceRepository depenceRepository;


    @Autowired
    private DepenceMapper depenceMapper;
    

    @Autowired
    private DepenceService depenceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDepenceMockMvc;

    private Depence depence;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DepenceResource depenceResource = new DepenceResource(depenceService);
        this.restDepenceMockMvc = MockMvcBuilders.standaloneSetup(depenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depence createEntity(EntityManager em) {
        Depence depence = new Depence()
            .name(DEFAULT_NAME)
            .montant(DEFAULT_MONTANT)
            .date(DEFAULT_DATE);
        return depence;
    }

    @Before
    public void initTest() {
        depence = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepence() throws Exception {
        int databaseSizeBeforeCreate = depenceRepository.findAll().size();

        // Create the Depence
        DepenceDTO depenceDTO = depenceMapper.toDto(depence);
        restDepenceMockMvc.perform(post("/api/depences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Depence in the database
        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeCreate + 1);
        Depence testDepence = depenceList.get(depenceList.size() - 1);
        assertThat(testDepence.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDepence.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDepence.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createDepenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depenceRepository.findAll().size();

        // Create the Depence with an existing ID
        depence.setId(1L);
        DepenceDTO depenceDTO = depenceMapper.toDto(depence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepenceMockMvc.perform(post("/api/depences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Depence in the database
        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenceRepository.findAll().size();
        // set the field null
        depence.setName(null);

        // Create the Depence, which fails.
        DepenceDTO depenceDTO = depenceMapper.toDto(depence);

        restDepenceMockMvc.perform(post("/api/depences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depenceDTO)))
            .andExpect(status().isBadRequest());

        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenceRepository.findAll().size();
        // set the field null
        depence.setMontant(null);

        // Create the Depence, which fails.
        DepenceDTO depenceDTO = depenceMapper.toDto(depence);

        restDepenceMockMvc.perform(post("/api/depences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depenceDTO)))
            .andExpect(status().isBadRequest());

        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = depenceRepository.findAll().size();
        // set the field null
        depence.setDate(null);

        // Create the Depence, which fails.
        DepenceDTO depenceDTO = depenceMapper.toDto(depence);

        restDepenceMockMvc.perform(post("/api/depences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depenceDTO)))
            .andExpect(status().isBadRequest());

        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDepences() throws Exception {
        // Initialize the database
        depenceRepository.saveAndFlush(depence);

        // Get all the depenceList
        restDepenceMockMvc.perform(get("/api/depences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depence.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    

    @Test
    @Transactional
    public void getDepence() throws Exception {
        // Initialize the database
        depenceRepository.saveAndFlush(depence);

        // Get the depence
        restDepenceMockMvc.perform(get("/api/depences/{id}", depence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(depence.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingDepence() throws Exception {
        // Get the depence
        restDepenceMockMvc.perform(get("/api/depences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepence() throws Exception {
        // Initialize the database
        depenceRepository.saveAndFlush(depence);

        int databaseSizeBeforeUpdate = depenceRepository.findAll().size();

        // Update the depence
        Depence updatedDepence = depenceRepository.findById(depence.getId()).get();
        // Disconnect from session so that the updates on updatedDepence are not directly saved in db
        em.detach(updatedDepence);
        updatedDepence
            .name(UPDATED_NAME)
            .montant(UPDATED_MONTANT)
            .date(UPDATED_DATE);
        DepenceDTO depenceDTO = depenceMapper.toDto(updatedDepence);

        restDepenceMockMvc.perform(put("/api/depences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depenceDTO)))
            .andExpect(status().isOk());

        // Validate the Depence in the database
        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeUpdate);
        Depence testDepence = depenceList.get(depenceList.size() - 1);
        assertThat(testDepence.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDepence.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDepence.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDepence() throws Exception {
        int databaseSizeBeforeUpdate = depenceRepository.findAll().size();

        // Create the Depence
        DepenceDTO depenceDTO = depenceMapper.toDto(depence);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDepenceMockMvc.perform(put("/api/depences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(depenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Depence in the database
        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDepence() throws Exception {
        // Initialize the database
        depenceRepository.saveAndFlush(depence);

        int databaseSizeBeforeDelete = depenceRepository.findAll().size();

        // Get the depence
        restDepenceMockMvc.perform(delete("/api/depences/{id}", depence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Depence> depenceList = depenceRepository.findAll();
        assertThat(depenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Depence.class);
        Depence depence1 = new Depence();
        depence1.setId(1L);
        Depence depence2 = new Depence();
        depence2.setId(depence1.getId());
        assertThat(depence1).isEqualTo(depence2);
        depence2.setId(2L);
        assertThat(depence1).isNotEqualTo(depence2);
        depence1.setId(null);
        assertThat(depence1).isNotEqualTo(depence2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepenceDTO.class);
        DepenceDTO depenceDTO1 = new DepenceDTO();
        depenceDTO1.setId(1L);
        DepenceDTO depenceDTO2 = new DepenceDTO();
        assertThat(depenceDTO1).isNotEqualTo(depenceDTO2);
        depenceDTO2.setId(depenceDTO1.getId());
        assertThat(depenceDTO1).isEqualTo(depenceDTO2);
        depenceDTO2.setId(2L);
        assertThat(depenceDTO1).isNotEqualTo(depenceDTO2);
        depenceDTO1.setId(null);
        assertThat(depenceDTO1).isNotEqualTo(depenceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(depenceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(depenceMapper.fromId(null)).isNull();
    }
}
