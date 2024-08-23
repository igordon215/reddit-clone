package com.example.redditclone.service;

import com.example.redditclone.domain.Post;
import com.example.redditclone.repository.PostRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.example.redditclone.domain.Post}.
 */
@Service
@Transactional
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Save a post.
     *
     * @param post the entity to save.
     * @return the persisted entity.
     */
    public Post save(Post post) {
        log.debug("Request to save Post : {}", post);
        return postRepository.save(post);
    }

    /**
     * Update a post.
     *
     * @param post the entity to save.
     * @return the persisted entity.
     */
    public Post update(Post post) {
        log.debug("Request to update Post : {}", post);
        return postRepository.save(post);
    }

    /**
     * Partially update a post.
     *
     * @param post the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Post> partialUpdate(Post post) {
        log.debug("Request to partially update Post : {}", post);

        return postRepository
            .findById(post.getId())
            .map(existingPost -> {
                if (post.getTitle() != null) {
                    existingPost.setTitle(post.getTitle());
                }
                if (post.getContent() != null) {
                    existingPost.setContent(post.getContent());
                }
                if (post.getCreatedDate() != null) {
                    existingPost.setCreatedDate(post.getCreatedDate());
                }

                return existingPost;
            })
            .map(postRepository::save);
    }

    /**
     * Get all the posts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        log.debug("Request to get all Posts");
        return postRepository.findAll(pageable);
    }

    /**
     * Get all the posts with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Post> findAllWithEagerRelationships(Pageable pageable) {
        return postRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one post by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Post> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the post by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }
}
