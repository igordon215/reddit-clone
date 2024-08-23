package com.example.redditclone.domain;

import static com.example.redditclone.domain.CommentTestSamples.*;
import static com.example.redditclone.domain.PostTestSamples.*;
import static com.example.redditclone.domain.SubredditTestSamples.*;
import static com.example.redditclone.domain.VoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.redditclone.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = getPostSample1();
        Post post2 = new Post();
        assertThat(post1).isNotEqualTo(post2);

        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);

        post2 = getPostSample2();
        assertThat(post1).isNotEqualTo(post2);
    }

    @Test
    void subredditTest() {
        Post post = getPostRandomSampleGenerator();
        Subreddit subredditBack = getSubredditRandomSampleGenerator();

        post.setSubreddit(subredditBack);
        assertThat(post.getSubreddit()).isEqualTo(subredditBack);

        post.subreddit(null);
        assertThat(post.getSubreddit()).isNull();
    }

    @Test
    void commentsTest() {
        Post post = getPostRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        post.addComments(commentBack);
        assertThat(post.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getPost()).isEqualTo(post);

        post.removeComments(commentBack);
        assertThat(post.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getPost()).isNull();

        post.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(post.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getPost()).isEqualTo(post);

        post.setComments(new HashSet<>());
        assertThat(post.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getPost()).isNull();
    }

    @Test
    void votesTest() {
        Post post = getPostRandomSampleGenerator();
        Vote voteBack = getVoteRandomSampleGenerator();

        post.addVotes(voteBack);
        assertThat(post.getVotes()).containsOnly(voteBack);
        assertThat(voteBack.getPost()).isEqualTo(post);

        post.removeVotes(voteBack);
        assertThat(post.getVotes()).doesNotContain(voteBack);
        assertThat(voteBack.getPost()).isNull();

        post.votes(new HashSet<>(Set.of(voteBack)));
        assertThat(post.getVotes()).containsOnly(voteBack);
        assertThat(voteBack.getPost()).isEqualTo(post);

        post.setVotes(new HashSet<>());
        assertThat(post.getVotes()).doesNotContain(voteBack);
        assertThat(voteBack.getPost()).isNull();
    }
}
