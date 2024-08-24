package com.example.redditclone.service;

import com.example.redditclone.domain.Post;
import com.example.redditclone.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {

    private final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post save(Post post) {
        log.debug("Request to save Post : {}", post);
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        log.debug("Request to get all Posts");
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Post> findOne(Long id) {
        log.debug("Request to get Post : {}", id);
        return postRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Post : {}", id);
        postRepository.deleteById(id);
    }
}
