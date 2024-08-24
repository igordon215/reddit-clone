package com.example.redditclone.web.rest;

import com.example.redditclone.domain.Subreddit;
import com.example.redditclone.service.SubredditService;
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
public class SubredditResource {

    private final Logger log = LoggerFactory.getLogger(SubredditResource.class);

    private static final String ENTITY_NAME = "subreddit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubredditService subredditService;

    public SubredditResource(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @PostMapping("/subreddits")
    public ResponseEntity<Subreddit> createSubreddit(@Valid @RequestBody Subreddit subreddit) throws URISyntaxException {
        log.debug("REST request to save Subreddit : {}", subreddit);
        if (subreddit.getId() != null) {
            throw new BadRequestAlertException("A new subreddit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Subreddit result = subredditService.save(subreddit);
        return ResponseEntity.created(new URI("/api/subreddits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/subreddits/{id}")
    public ResponseEntity<Subreddit> updateSubreddit(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Subreddit subreddit
    ) throws URISyntaxException {
        log.debug("REST request to update Subreddit : {}, {}", id, subreddit);
        if (subreddit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subreddit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subredditService.findOne(id).isPresent()) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Subreddit result = subredditService.save(subreddit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subreddit.getId().toString()))
            .body(result);
    }

    @GetMapping("/subreddits")
    public List<Subreddit> getAllSubreddits() {
        log.debug("REST request to get all Subreddits");
        return subredditService.findAll();
    }

    @GetMapping("/subreddits/{id}")
    public ResponseEntity<Subreddit> getSubreddit(@PathVariable Long id) {
        log.debug("REST request to get Subreddit : {}", id);
        Optional<Subreddit> subreddit = subredditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subreddit);
    }

    @DeleteMapping("/subreddits/{id}")
    public ResponseEntity<Void> deleteSubreddit(@PathVariable Long id) {
        log.debug("REST request to delete Subreddit : {}", id);
        subredditService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
