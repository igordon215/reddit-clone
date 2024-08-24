package com.example.redditclone.service;

import com.example.redditclone.domain.Vote;
import com.example.redditclone.repository.VoteRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoteService {

    private final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote save(Vote vote) {
        log.debug("Request to save Vote : {}", vote);
        return voteRepository.save(vote);
    }

    @Transactional(readOnly = true)
    public List<Vote> findAll() {
        log.debug("Request to get all Votes");
        return voteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Vote> findOne(Long id) {
        log.debug("Request to get Vote : {}", id);
        return voteRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Vote : {}", id);
        voteRepository.deleteById(id);
    }
}
