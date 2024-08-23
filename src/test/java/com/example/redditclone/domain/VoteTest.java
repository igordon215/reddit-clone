package com.example.redditclone.domain;

import static com.example.redditclone.domain.PostTestSamples.*;
import static com.example.redditclone.domain.VoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.redditclone.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vote.class);
        Vote vote1 = getVoteSample1();
        Vote vote2 = new Vote();
        assertThat(vote1).isNotEqualTo(vote2);

        vote2.setId(vote1.getId());
        assertThat(vote1).isEqualTo(vote2);

        vote2 = getVoteSample2();
        assertThat(vote1).isNotEqualTo(vote2);
    }

    @Test
    void postTest() {
        Vote vote = getVoteRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        vote.setPost(postBack);
        assertThat(vote.getPost()).isEqualTo(postBack);

        vote.post(null);
        assertThat(vote.getPost()).isNull();
    }
}
