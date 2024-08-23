package com.example.redditclone.service;

import com.example.redditclone.domain.Vote;
import com.example.redditclone.repository.VoteRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.redditclone.domain.Vote}.
 */
@Service
@Transactional
public class VoteService {

    private static final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    /**
     * Save a vote.
     *
     * @param vote the entity to save.
     * @return the persisted entity.
     */
    public Vote save(Vote vote) {
        log.debug("Request to save Vote : {}", vote);
        return voteRepository.save(vote);
    }

    /**
     * Update a vote.
     *
     * @param vote the entity to save.
     * @return the persisted entity.
     */
    public Vote update(Vote vote) {
        log.debug("Request to update Vote : {}", vote);
        return voteRepository.save(vote);
    }

    /**
     * Partially update a vote.
     *
     * @param vote the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vote> partialUpdate(Vote vote) {
        log.debug("Request to partially update Vote : {}", vote);

        return voteRepository
            .findById(vote.getId())
            .map(existingVote -> {
                if (vote.getVoteType() != null) {
                    existingVote.setVoteType(vote.getVoteType());
                }

                return existingVote;
            })
            .map(voteRepository::save);
    }

    /**
     * Get all the votes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Vote> findAll() {
        log.debug("Request to get all Votes");
        return voteRepository.findAll();
    }

    /**
     * Get all the votes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Vote> findAllWithEagerRelationships(Pageable pageable) {
        return voteRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one vote by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vote> findOne(Long id) {
        log.debug("Request to get Vote : {}", id);
        return voteRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the vote by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Vote : {}", id);
        voteRepository.deleteById(id);
    }
}
