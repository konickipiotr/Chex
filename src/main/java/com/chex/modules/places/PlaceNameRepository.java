package com.chex.modules.places;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceNameRepository extends JpaRepository<PlaceName, String> {
}
