package com.asanka.tutor.web.rest;

import com.asanka.tutor.OnlineTutorApp;

import com.asanka.tutor.domain.Chapter;
import com.asanka.tutor.repository.ChapterRepository;
import com.asanka.tutor.service.ChapterService;
import com.asanka.tutor.web.rest.errors.ExceptionTranslator;

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

import java.util.List;


import static com.asanka.tutor.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ChapterResource REST controller.
 *
 * @see ChapterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OnlineTutorApp.class)
public class ChapterResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CHAPTER_NUMBER = 1;
    private static final Integer UPDATED_CHAPTER_NUMBER = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COURSE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_ID = "BBBBBBBBBB";

    @Autowired
    private ChapterRepository chapterRepository;

    

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restChapterMockMvc;

    private Chapter chapter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChapterResource chapterResource = new ChapterResource(chapterService);
        this.restChapterMockMvc = MockMvcBuilders.standaloneSetup(chapterResource)
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
    public static Chapter createEntity() {
        Chapter chapter = new Chapter()
            .name(DEFAULT_NAME)
            .chapterNumber(DEFAULT_CHAPTER_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .courseID(DEFAULT_COURSE_ID);
        return chapter;
    }

    @Before
    public void initTest() {
        chapterRepository.deleteAll();
        chapter = createEntity();
    }

    @Test
    public void createChapter() throws Exception {
        int databaseSizeBeforeCreate = chapterRepository.findAll().size();

        // Create the Chapter
        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapter)))
            .andExpect(status().isCreated());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeCreate + 1);
        Chapter testChapter = chapterList.get(chapterList.size() - 1);
        assertThat(testChapter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChapter.getChapterNumber()).isEqualTo(DEFAULT_CHAPTER_NUMBER);
        assertThat(testChapter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testChapter.getCourseID()).isEqualTo(DEFAULT_COURSE_ID);
    }

    @Test
    public void createChapterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chapterRepository.findAll().size();

        // Create the Chapter with an existing ID
        chapter.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapter)))
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = chapterRepository.findAll().size();
        // set the field null
        chapter.setName(null);

        // Create the Chapter, which fails.

        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapter)))
            .andExpect(status().isBadRequest());

        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkChapterNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = chapterRepository.findAll().size();
        // set the field null
        chapter.setChapterNumber(null);

        // Create the Chapter, which fails.

        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapter)))
            .andExpect(status().isBadRequest());

        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = chapterRepository.findAll().size();
        // set the field null
        chapter.setDescription(null);

        // Create the Chapter, which fails.

        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapter)))
            .andExpect(status().isBadRequest());

        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkCourseIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = chapterRepository.findAll().size();
        // set the field null
        chapter.setCourseID(null);

        // Create the Chapter, which fails.

        restChapterMockMvc.perform(post("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapter)))
            .andExpect(status().isBadRequest());

        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllChapters() throws Exception {
        // Initialize the database
        chapterRepository.save(chapter);

        // Get all the chapterList
        restChapterMockMvc.perform(get("/api/chapters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chapter.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].chapterNumber").value(hasItem(DEFAULT_CHAPTER_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].courseID").value(hasItem(DEFAULT_COURSE_ID.toString())));
    }
    

    @Test
    public void getChapter() throws Exception {
        // Initialize the database
        chapterRepository.save(chapter);

        // Get the chapter
        restChapterMockMvc.perform(get("/api/chapters/{id}", chapter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chapter.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.chapterNumber").value(DEFAULT_CHAPTER_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.courseID").value(DEFAULT_COURSE_ID.toString()));
    }
    @Test
    public void getNonExistingChapter() throws Exception {
        // Get the chapter
        restChapterMockMvc.perform(get("/api/chapters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateChapter() throws Exception {
        // Initialize the database
        chapterService.save(chapter);

        int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

        // Update the chapter
        Chapter updatedChapter = chapterRepository.findById(chapter.getId()).get();
        updatedChapter
            .name(UPDATED_NAME)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .courseID(UPDATED_COURSE_ID);

        restChapterMockMvc.perform(put("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChapter)))
            .andExpect(status().isOk());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
        Chapter testChapter = chapterList.get(chapterList.size() - 1);
        assertThat(testChapter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChapter.getChapterNumber()).isEqualTo(UPDATED_CHAPTER_NUMBER);
        assertThat(testChapter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testChapter.getCourseID()).isEqualTo(UPDATED_COURSE_ID);
    }

    @Test
    public void updateNonExistingChapter() throws Exception {
        int databaseSizeBeforeUpdate = chapterRepository.findAll().size();

        // Create the Chapter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChapterMockMvc.perform(put("/api/chapters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chapter)))
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteChapter() throws Exception {
        // Initialize the database
        chapterService.save(chapter);

        int databaseSizeBeforeDelete = chapterRepository.findAll().size();

        // Get the chapter
        restChapterMockMvc.perform(delete("/api/chapters/{id}", chapter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Chapter> chapterList = chapterRepository.findAll();
        assertThat(chapterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chapter.class);
        Chapter chapter1 = new Chapter();
        chapter1.setId("id1");
        Chapter chapter2 = new Chapter();
        chapter2.setId(chapter1.getId());
        assertThat(chapter1).isEqualTo(chapter2);
        chapter2.setId("id2");
        assertThat(chapter1).isNotEqualTo(chapter2);
        chapter1.setId(null);
        assertThat(chapter1).isNotEqualTo(chapter2);
    }
}
