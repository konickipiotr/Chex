package com.chex.modules.post.repository;

import com.chex.modules.post.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {
    boolean existsByPostidAndUserid(Long postid, Long userid);
    void deleteByPostidAndUserid(Long postid, Long userid);
    List<Star> findByPostid(Long postid);

    boolean existsByUserid(Long userid);
    void deleteByUserid(Long userid);
    void deleteByPostid(Long postid);
}
