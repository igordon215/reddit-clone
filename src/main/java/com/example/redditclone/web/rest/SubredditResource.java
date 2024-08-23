package com.example.redditclone.web.rest;

import com.example.redditclone.domain.Subreddit;
import com.example.redditclone.repository.SubredditRepository;
import com.example.redditclone.service.SubredditService;
import com.example.redditclone.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link com.example.redditclone.domain.Subreddit}.
 */
@RestController
@RequestMapping("/api/subreddits")
public class SubredditResource {

    private static final Logger log = LoggerFactory.getLogger(SubredditResource.class);

    private static final String ENTITY_NAME = "subreddit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubredditService subredditService;

    private final SubredditRepository subredditRepository;

    public SubredditResource(SubredditService subredditService, SubredditRepository subredditRepository) {
        this.subredditService = subredditService;
        this.subredditRepository = subredditRepository;
    }

    /**
     * {@code POST  /subreddits} : Create a new subreddit.
     *
     * @param subreddit the subreddit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subreddit, or with status {@code 400 (Bad Request)} if the subreddit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Subreddit> createSubreddit(@Valid @RequestBody Subreddit subreddit) throws URISyntaxException {
        log.debug("REST request to save Subreddit : {}", subreddit);
        if (subreddit.getId() != null) {
            throw new BadRequestAlertException("A new subreddit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subreddit = subredditService.save(subreddit);
        return ResponseEntity.created(new URI("/api/subreddits/" + subreddit.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, subreddit.getId().toString()))
            .body(subreddit);
    }

    /**
     * {@code PUT  /subreddits/:id} : Updates an existing subreddit.
     *
     * @param id the id of the subreddit to save.
     * @param subreddit the subreddit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subreddit,
     * or with status {@code 400 (Bad Request)} if the subreddit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subreddit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
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

        if (!subredditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subreddit = subredditService.update(subreddit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subreddit.getId().toString()))
            .body(subreddit);
    }

    /**
     * {@code PATCH  /subreddits/:id} : Partial updates given fields of an existing subreddit, field will ignore if it is null
     *
     * @param id the id of the subreddit to save.
     * @param subreddit the subreddit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subreddit,
     * or with status {@code 400 (Bad Request)} if the subreddit is not valid,
     * or with status {@code 404 (Not Found)} if the subreddit is not found,
     * or with status {@code 500 (Internal Server Error)} if the subreddit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Subreddit> partialUpdateSubreddit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Subreddit subreddit
    ) throws URISyntaxException {
        log.debug("REST request to partial update Subreddit partially : {}, {}", id, subreddit);
        if (subreddit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subreddit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subredditRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Subreddit> result = subredditService.partialUpdate(subreddit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subreddit.getId().toString())
        );
    }

    /**
     * {@code GET  /subreddits} : get all the subreddits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subreddits in body.
     */
    @GetMapping("")
    public List<Subreddit> getAllSubreddits() {
        log.debug("REST request to get all Subreddits");
        return subredditService.findAll();
    }

    /**
     * {@code GET  /subreddits/:id} : get the "id" subreddit.
     *
     * @param id the id of the subreddit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subreddit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Subreddit> getSubreddit(@PathVariable("id") Long id) {
        log.debug("REST request to get Subreddit : {}", id);
        Optional<Subreddit> subreddit = subredditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subreddit);
    }

    /**
     * {@code DELETE  /subreddits/:id} : delete the "id" subreddit.
     *
     * @param id the id of the subreddit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubreddit(@PathVariable("id") Long id) {
        log.debug("REST request to delete Subreddit : {}", id);
        subredditService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
