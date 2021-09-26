package com.chex.modules.post.repository;

import com.chex.modules.post.model.PostPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPhotoRepository extends JpaRepository<PostPhoto, Long> {
    List<PostPhoto> findByPostid(Long postid);
    List<PostPhoto> findByUserid(Long userid);
}
