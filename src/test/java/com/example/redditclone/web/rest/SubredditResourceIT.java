package com.example.redditclone.web.rest;

import static com.example.redditclone.domain.SubredditAsserts.*;
import static com.example.redditclone.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.redditclone.IntegrationTest;
import com.example.redditclone.domain.Subreddit;
import com.example.redditclone.repository.SubredditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubredditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubredditResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/subreddits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubredditMockMvc;

    private Subreddit subreddit;

    private Subreddit insertedSubreddit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subreddit createEntity(EntityManager em) {
        Subreddit subreddit = new Subreddit().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return subreddit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subreddit createUpdatedEntity(EntityManager em) {
        Subreddit subreddit = new Subreddit().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return subreddit;
    }

    @BeforeEach
    public void initTest() {
        subreddit = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSubreddit != null) {
            subredditRepository.delete(insertedSubreddit);
            insertedSubreddit = null;
        }
    }

    @Test
    @Transactional
    void createSubreddit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Subreddit
        var returnedSubreddit = om.readValue(
            restSubredditMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subreddit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Subreddit.class
        );

        // Validate the Subreddit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubredditUpdatableFieldsEquals(returnedSubreddit, getPersistedSubreddit(returnedSubreddit));

        insertedSubreddit = returnedSubreddit;
    }

    @Test
    @Transactional
    void createSubredditWithExistingId() throws Exception {
        // Create the Subreddit with an existing ID
        subreddit.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubredditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subreddit)))
            .andExpect(status().isBadRequest());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subreddit.setName(null);

        // Create the Subreddit, which fails.

        restSubredditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subreddit)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubreddits() throws Exception {
        // Initialize the database
        insertedSubreddit = subredditRepository.saveAndFlush(subreddit);

        // Get all the subredditList
        restSubredditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subreddit.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSubreddit() throws Exception {
        // Initialize the database
        insertedSubreddit = subredditRepository.saveAndFlush(subreddit);

        // Get the subreddit
        restSubredditMockMvc
            .perform(get(ENTITY_API_URL_ID, subreddit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subreddit.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSubreddit() throws Exception {
        // Get the subreddit
        restSubredditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubreddit() throws Exception {
        // Initialize the database
        insertedSubreddit = subredditRepository.saveAndFlush(subreddit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subreddit
        Subreddit updatedSubreddit = subredditRepository.findById(subreddit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubreddit are not directly saved in db
        em.detach(updatedSubreddit);
        updatedSubreddit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSubredditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubreddit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubreddit))
            )
            .andExpect(status().isOk());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubredditToMatchAllProperties(updatedSubreddit);
    }

    @Test
    @Transactional
    void putNonExistingSubreddit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subreddit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubredditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subreddit.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subreddit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubreddit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subreddit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubredditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subreddit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubreddit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subreddit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubredditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subreddit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubredditWithPatch() throws Exception {
        // Initialize the database
        insertedSubreddit = subredditRepository.saveAndFlush(subreddit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subreddit using partial update
        Subreddit partialUpdatedSubreddit = new Subreddit();
        partialUpdatedSubreddit.setId(subreddit.getId());

        partialUpdatedSubreddit.description(UPDATED_DESCRIPTION);

        restSubredditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubreddit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubreddit))
            )
            .andExpect(status().isOk());

        // Validate the Subreddit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubredditUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSubreddit, subreddit),
            getPersistedSubreddit(subreddit)
        );
    }

    @Test
    @Transactional
    void fullUpdateSubredditWithPatch() throws Exception {
        // Initialize the database
        insertedSubreddit = subredditRepository.saveAndFlush(subreddit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subreddit using partial update
        Subreddit partialUpdatedSubreddit = new Subreddit();
        partialUpdatedSubreddit.setId(subreddit.getId());

        partialUpdatedSubreddit.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restSubredditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubreddit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubreddit))
            )
            .andExpect(status().isOk());

        // Validate the Subreddit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubredditUpdatableFieldsEquals(partialUpdatedSubreddit, getPersistedSubreddit(partialUpdatedSubreddit));
    }

    @Test
    @Transactional
    void patchNonExistingSubreddit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subreddit.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubredditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subreddit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subreddit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubreddit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subreddit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubredditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subreddit))
            )
            .andExpect(status().isBadRequest());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubreddit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subreddit.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubredditMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subreddit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Subreddit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubreddit() throws Exception {
        // Initialize the database
        insertedSubreddit = subredditRepository.saveAndFlush(subreddit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subreddit
        restSubredditMockMvc
            .perform(delete(ENTITY_API_URL_ID, subreddit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subredditRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Subreddit getPersistedSubreddit(Subreddit subreddit) {
        return subredditRepository.findById(subreddit.getId()).orElseThrow();
    }

    protected void assertPersistedSubredditToMatchAllProperties(Subreddit expectedSubreddit) {
        assertSubredditAllPropertiesEquals(expectedSubreddit, getPersistedSubreddit(expectedSubreddit));
    }

    protected void assertPersistedSubredditToMatchUpdatableProperties(Subreddit expectedSubreddit) {
        assertSubredditAllUpdatablePropertiesEquals(expectedSubreddit, getPersistedSubreddit(expectedSubreddit));
    }
}
