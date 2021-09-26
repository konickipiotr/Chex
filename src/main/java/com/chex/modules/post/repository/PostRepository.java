package com.chex.modules.post.repository;

import com.chex.modules.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.userid in :ids and p.postvisibility != 'PRIVATE' order by p.created desc")
    List<Post> findByIdsOrderByTimestamp(@Param("ids") List<Long> ids, Pageable pageable);

    @Query("select p from Post p where p.postvisibility = 'PUBLIC' order by p.created desc")
    List<Post> findAllOrderByCreated(Pageable pageable);

    List<Post> findByUserid(Long userid);
}
