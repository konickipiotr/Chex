package com.chex.modules.post.repository;

import com.chex.modules.post.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentRepositoryTest {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentRepositoryTest(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @BeforeEach
    void setUp() {
        this.commentRepository.deleteAll();
    }

    @Test
    void commentsAreSortedByTimestamp() {

        Long postid1 = 88L;
        Long postid2 = 99L;
        Long userid = 555L;


        this.commentRepository.save(new Comment(postid1, userid, "elo", LocalDateTime.of(2021, 5, 23, 10, 30)));
        this.commentRepository.save(new Comment(postid2, userid, "melo", LocalDateTime.of(2021, 5, 23, 10, 30)));
        this.commentRepository.save(new Comment(postid1, userid, "hello", LocalDateTime.of(2021, 5, 22, 10, 30)));
        this.commentRepository.save(new Comment(postid1, userid, "zzz", LocalDateTime.of(2022, 5, 22, 10, 30)));

        List<Comment> comments = this.commentRepository.findAllByPostidOrderByCreatedDesc(postid1);
        assertEquals(3, comments.size());
        assertEquals("zzz", comments.get(0).getContent());
        assertEquals("elo", comments.get(1).getContent());
        assertEquals("hello", comments.get(2).getContent());

    }
}