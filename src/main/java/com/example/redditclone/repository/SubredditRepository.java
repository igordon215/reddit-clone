package com.example.redditclone.repository;

import com.example.redditclone.domain.Subreddit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Subreddit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {}
