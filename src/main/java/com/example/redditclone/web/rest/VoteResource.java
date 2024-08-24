package com.example.redditclone.web.rest;

import com.example.redditclone.domain.Vote;
import com.example.redditclone.service.VoteService;
import com.example.redditclone.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class VoteResource {

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    private static final String ENTITY_NAME = "vote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoteService voteService;

    public VoteResource(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/votes")
    public ResponseEntity<Vote> createVote(@Valid @RequestBody Vote vote) throws URISyntaxException {
        log.debug("REST request to save Vote : {}", vote);
        if (vote.getId() != null) {
            throw new BadRequestAlertException("A new vote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vote result = voteService.save(vote);
        return ResponseEntity.created(new URI("/api/votes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/votes/{id}")
    public ResponseEntity<Vote> updateVote(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Vote vote)
        throws URISyntaxException {
        log.debug("REST request to update Vote : {}, {}", id, vote);
        if (vote.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vote.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voteService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vote result = voteService.save(vote);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vote.getId().toString()))
            .body(result);
    }

    @GetMapping("/votes")
    public List<Vote> getAllVotes() {
        log.debug("REST request to get all Votes");
        return voteService.findAll();
    }

    @GetMapping("/votes/{id}")
    public ResponseEntity<Vote> getVote(@PathVariable Long id) {
        log.debug("REST request to get Vote : {}", id);
        Optional<Vote> vote = voteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vote);
    }

    @DeleteMapping("/votes/{id}")
    public ResponseEntity<Void> deleteVote(@PathVariable Long id) {
        log.debug("REST request to delete Vote : {}", id);
        voteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
