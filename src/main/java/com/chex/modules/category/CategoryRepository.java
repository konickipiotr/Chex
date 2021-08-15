package com.chex.modules.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByEng(String category);
    boolean existsByPl(String category);
    Category findByEng(String category);
    Category findByPl(String category);
}
