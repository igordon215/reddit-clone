package com.example.redditclone.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VoteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vote getVoteSample1() {
        return new Vote().id(1L).voteType("voteType1");
    }

    public static Vote getVoteSample2() {
        return new Vote().id(2L).voteType("voteType2");
    }

    public static Vote getVoteRandomSampleGenerator() {
        return new Vote().id(longCount.incrementAndGet()).voteType(UUID.randomUUID().toString());
    }
}
