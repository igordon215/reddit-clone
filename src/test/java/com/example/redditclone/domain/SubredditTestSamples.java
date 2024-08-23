package com.example.redditclone.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubredditTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Subreddit getSubredditSample1() {
        return new Subreddit().id(1L).name("name1").description("description1");
    }

    public static Subreddit getSubredditSample2() {
        return new Subreddit().id(2L).name("name2").description("description2");
    }

    public static Subreddit getSubredditRandomSampleGenerator() {
        return new Subreddit().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
