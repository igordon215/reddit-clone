package com.example.redditclone.domain;

import static com.example.redditclone.domain.PostTestSamples.*;
import static com.example.redditclone.domain.SubredditTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.redditclone.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SubredditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subreddit.class);
        Subreddit subreddit1 = getSubredditSample1();
        Subreddit subreddit2 = new Subreddit();
        assertThat(subreddit1).isNotEqualTo(subreddit2);

        subreddit2.setId(subreddit1.getId());
        assertThat(subreddit1).isEqualTo(subreddit2);

        subreddit2 = getSubredditSample2();
        assertThat(subreddit1).isNotEqualTo(subreddit2);
    }

    @Test
    void postsTest() {
        Subreddit subreddit = getSubredditRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        subreddit.addPosts(postBack);
        assertThat(subreddit.getPosts()).containsOnly(postBack);
        assertThat(postBack.getSubreddit()).isEqualTo(subreddit);

        subreddit.removePosts(postBack);
        assertThat(subreddit.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getSubreddit()).isNull();

        subreddit.posts(new HashSet<>(Set.of(postBack)));
        assertThat(subreddit.getPosts()).containsOnly(postBack);
        assertThat(postBack.getSubreddit()).isEqualTo(subreddit);

        subreddit.setPosts(new HashSet<>());
        assertThat(subreddit.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getSubreddit()).isNull();
    }
}
