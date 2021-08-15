package com.chex.modules.places;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {

    @Query(value = "From Place where (latitude between :Slatitude and :Nlatitude)  and (longitude between :Wlongitude and :Elongitude)")
    List<Place> filterCoords(@Param("Nlatitude")double Nlatitude,
                             @Param("Slatitude")double Slatitude,
                             @Param("Wlongitude")double Wlongitude,
                             @Param("Elongitude")double Elongitude
    );

    @Query(value = "From Place where id like '%.000.000.000.00000'")
    List<Place> getContinents();

    @Query(value = "from Place where (id like CONCAT(:continentid,'.___.000.000.00000')) and (id not like CONCAT(:continentid,'.000.000.000.00000'))")
    List<Place> getAllCountriesFromContinent(@Param("continentid") String continentid);

    @Query(value = "from Place where (id like CONCAT(:countryid,'.___.000.00000')) and (id not like CONCAT(:countryid,'.000.000.00000'))")
    List<Place> getAllProvincesFromCountry(@Param("countryid") String countryid);

    @Query(value = "from Place where (id like CONCAT(:provinceid,'.___.00000')) and (id not like CONCAT(:provinceid,'.000.00000'))")
    List<Place> getRegionsFromProvince(@Param("provinceid") String provinceid);

    @Query(value = "from Place where (id like CONCAT(:regionid,'._____')) and (id not like CONCAT(:regionid,'.00000'))")
    List<Place> getPlacesFromRegion(@Param("regionid") String regionid);

    @Query(value = "from Place where id like CONCAT(:parentid,'.%')")
    List<Place> getAllChildrenPlaces(@Param("parentid")String paretnid);
}
