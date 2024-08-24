package com.example.redditclone.service;

import com.example.redditclone.domain.Subreddit;
import com.example.redditclone.repository.SubredditRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubredditService {

    private final Logger log = LoggerFactory.getLogger(SubredditService.class);

    private final SubredditRepository subredditRepository;

    public SubredditService(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    public Subreddit save(Subreddit subreddit) {
        log.debug("Request to save Subreddit : {}", subreddit);
        return subredditRepository.save(subreddit);
    }

    @Transactional(readOnly = true)
    public List<Subreddit> findAll() {
        log.debug("Request to get all Subreddits");
        return subredditRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Subreddit> findOne(Long id) {
        log.debug("Request to get Subreddit : {}", id);
        return subredditRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Subreddit : {}", id);
        subredditRepository.deleteById(id);
    }
}
