package com.chex.modules.post.repository;

import com.chex.modules.post.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostRepositoryTest {

    private final PostRepository postRepository;

    @Autowired
    PostRepositoryTest(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @BeforeEach
    void setUp() {
        this.postRepository.deleteAll();
    }

    @Test
    void findFriendsPosts() {
        List<Long> ids = Arrays.asList(3L, 33L, 333L);

        this.postRepository.save(new Post(3L, LocalDateTime.of(2021, 8, 12, 5, 28)));
        this.postRepository.save(new Post(4L, LocalDateTime.of(2021, 8, 12, 4, 28)));
        this.postRepository.save(new Post(33L, LocalDateTime.of(2021, 8, 13, 5, 28)));
        this.postRepository.save(new Post(55L, LocalDateTime.of(2021, 8, 13, 5, 28)));
        this.postRepository.save(new Post(333L, LocalDateTime.of(2021, 8, 12, 4, 28)));

        List<Post> list = postRepository.findByIdsOrderByTimestamp(ids, PageRequest.of(0, 5));
        assertEquals(3, list.size());
        assertEquals(33L, list.get(0).getUserid());
        assertEquals(3L, list.get(1).getUserid());
        assertEquals(333L, list.get(2).getUserid());

    }

    @Test
    void findallPosts() {
        this.postRepository.save(new Post(3L, LocalDateTime.of(2021, 8, 12, 5, 28)));
        this.postRepository.save(new Post(4L, LocalDateTime.of(2021, 8, 12, 4, 28)));
        this.postRepository.save(new Post(33L, LocalDateTime.of(2021, 8, 13, 5, 28)));
        this.postRepository.save(new Post(55L, LocalDateTime.of(2021, 8, 13, 5, 28)));
        this.postRepository.save(new Post(333L, LocalDateTime.of(2021, 8, 12, 4, 28)));

        List<Post> list = postRepository.findAllOrderByCreated(PageRequest.of(0, 5));
        assertEquals(5, list.size());
        list = postRepository.findAllOrderByCreated(PageRequest.of(0, 3));
        assertEquals(3, list.size());

    }
}