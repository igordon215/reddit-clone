package com.example.redditclone.service;

import com.example.redditclone.domain.Subreddit;
import com.example.redditclone.repository.SubredditRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.redditclone.domain.Subreddit}.
 */
@Service
@Transactional
public class SubredditService {

    private static final Logger log = LoggerFactory.getLogger(SubredditService.class);

    private final SubredditRepository subredditRepository;

    public SubredditService(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    /**
     * Save a subreddit.
     *
     * @param subreddit the entity to save.
     * @return the persisted entity.
     */
    public Subreddit save(Subreddit subreddit) {
        log.debug("Request to save Subreddit : {}", subreddit);
        return subredditRepository.save(subreddit);
    }

    /**
     * Update a subreddit.
     *
     * @param subreddit the entity to save.
     * @return the persisted entity.
     */
    public Subreddit update(Subreddit subreddit) {
        log.debug("Request to update Subreddit : {}", subreddit);
        return subredditRepository.save(subreddit);
    }

    /**
     * Partially update a subreddit.
     *
     * @param subreddit the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Subreddit> partialUpdate(Subreddit subreddit) {
        log.debug("Request to partially update Subreddit : {}", subreddit);

        return subredditRepository
            .findById(subreddit.getId())
            .map(existingSubreddit -> {
                if (subreddit.getName() != null) {
                    existingSubreddit.setName(subreddit.getName());
                }
                if (subreddit.getDescription() != null) {
                    existingSubreddit.setDescription(subreddit.getDescription());
                }

                return existingSubreddit;
            })
            .map(subredditRepository::save);
    }

    /**
     * Get all the subreddits.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Subreddit> findAll() {
        log.debug("Request to get all Subreddits");
        return subredditRepository.findAll();
    }

    /**
     * Get one subreddit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Subreddit> findOne(Long id) {
        log.debug("Request to get Subreddit : {}", id);
        return subredditRepository.findById(id);
    }

    /**
     * Delete the subreddit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Subreddit : {}", id);
        subredditRepository.deleteById(id);
    }
}
