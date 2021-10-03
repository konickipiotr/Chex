package com.chex.modules.places.repository;

import com.chex.lang.LanguageGetter;
import com.chex.modules.places.model.PlaceName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceNameRepository extends JpaRepository<PlaceName, String> {
}
