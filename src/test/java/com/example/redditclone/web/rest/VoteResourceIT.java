package com.example.redditclone.web.rest;

import static com.example.redditclone.domain.VoteAsserts.*;
import static com.example.redditclone.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.redditclone.IntegrationTest;
import com.example.redditclone.domain.Vote;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VoteRepository;
import com.example.redditclone.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VoteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VoteResourceIT {

    private static final String DEFAULT_VOTE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VOTE_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/votes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private VoteRepository voteRepositoryMock;

    @Mock
    private VoteService voteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoteMockMvc;

    private Vote vote;

    private Vote insertedVote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vote createEntity(EntityManager em) {
        Vote vote = new Vote().voteType(DEFAULT_VOTE_TYPE);
        return vote;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vote createUpdatedEntity(EntityManager em) {
        Vote vote = new Vote().voteType(UPDATED_VOTE_TYPE);
        return vote;
    }

    @BeforeEach
    public void initTest() {
        vote = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVote != null) {
            voteRepository.delete(insertedVote);
            insertedVote = null;
        }
    }

    @Test
    @Transactional
    void createVote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vote
        var returnedVote = om.readValue(
            restVoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vote)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Vote.class
        );

        // Validate the Vote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVoteUpdatableFieldsEquals(returnedVote, getPersistedVote(returnedVote));

        insertedVote = returnedVote;
    }

    @Test
    @Transactional
    void createVoteWithExistingId() throws Exception {
        // Create the Vote with an existing ID
        vote.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vote)))
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVoteTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vote.setVoteType(null);

        // Create the Vote, which fails.

        restVoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vote)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVotes() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        // Get all the voteList
        restVoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vote.getId().intValue())))
            .andExpect(jsonPath("$.[*].voteType").value(hasItem(DEFAULT_VOTE_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVotesWithEagerRelationshipsIsEnabled() throws Exception {
        when(voteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(voteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVotesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(voteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVoteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(voteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVote() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        // Get the vote
        restVoteMockMvc
            .perform(get(ENTITY_API_URL_ID, vote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vote.getId().intValue()))
            .andExpect(jsonPath("$.voteType").value(DEFAULT_VOTE_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingVote() throws Exception {
        // Get the vote
        restVoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVote() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vote
        Vote updatedVote = voteRepository.findById(vote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVote are not directly saved in db
        em.detach(updatedVote);
        updatedVote.voteType(UPDATED_VOTE_TYPE);

        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVoteToMatchAllProperties(updatedVote);
    }

    @Test
    @Transactional
    void putNonExistingVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(put(ENTITY_API_URL_ID, vote.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vote)))
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoteWithPatch() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vote using partial update
        Vote partialUpdatedVote = new Vote();
        partialUpdatedVote.setId(vote.getId());

        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVote, vote), getPersistedVote(vote));
    }

    @Test
    @Transactional
    void fullUpdateVoteWithPatch() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vote using partial update
        Vote partialUpdatedVote = new Vote();
        partialUpdatedVote.setId(vote.getId());

        partialUpdatedVote.voteType(UPDATED_VOTE_TYPE);

        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVote))
            )
            .andExpect(status().isOk());

        // Validate the Vote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoteUpdatableFieldsEquals(partialUpdatedVote, getPersistedVote(partialUpdatedVote));
    }

    @Test
    @Transactional
    void patchNonExistingVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(patch(ENTITY_API_URL_ID, vote.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vote)))
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vote))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vote.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vote)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVote() throws Exception {
        // Initialize the database
        insertedVote = voteRepository.saveAndFlush(vote);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vote
        restVoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, vote.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return voteRepository.count();
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

    protected Vote getPersistedVote(Vote vote) {
        return voteRepository.findById(vote.getId()).orElseThrow();
    }

    protected void assertPersistedVoteToMatchAllProperties(Vote expectedVote) {
        assertVoteAllPropertiesEquals(expectedVote, getPersistedVote(expectedVote));
    }

    protected void assertPersistedVoteToMatchUpdatableProperties(Vote expectedVote) {
        assertVoteAllUpdatablePropertiesEquals(expectedVote, getPersistedVote(expectedVote));
    }
}
