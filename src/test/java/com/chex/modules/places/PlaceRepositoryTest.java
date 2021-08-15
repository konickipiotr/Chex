package com.chex.modules.places;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlaceRepositoryTest {

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceRepositoryTest(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @BeforeEach
    public void setUp() {
        placeRepository.deleteAll();
    }

    @Test
    public void findall_on_empty_db_return_empty_list(){
        List<Place> placelist = placeRepository.findAll();
        assertNotNull(placelist);
        assertEquals(0, placelist.size());
    }

    @Test
    public void get_place_from_empty_db_return_empty_list(){
        String value = "EU";
        List<Place> placelist = placeRepository.getAllCountriesFromContinent(value);
        assertNotNull(placelist);
        assertEquals(0, placelist.size());
    }

    @Test
    public void get_not_existing_element_from_db_return_empty_list(){
        String value = "EU";
        placeRepository.save(new Place("NA.PL0.000.000.0000"));

        assertEquals(1, placeRepository.findAll().size());

        List<Place> placelist = placeRepository.getAllCountriesFromContinent(value);
        assertEquals(0, placelist.size());
    }

    @Test
    public void return_existing_place(){
        String value = "EU";
        String id = "EU.PL0.000.000.00000";
        placeRepository.save(new Place( id));

        assertEquals(1, placeRepository.findAll().size());
        List<Place> placelist = placeRepository.getAllCountriesFromContinent(value);
        assertEquals(1, placelist.size());
        assertEquals(placelist.get(0).getId(), id);
    }

    @Test
    public void return_three_existing_places_from_4_elemnt_db(){
        String value = "EU";
        String id1 = "EU.PL0.000.000.00000";
        String id2 = "EU.GER.000.000.00000";
        String id3 = "EU.ITA.000.000.00000";
        String id4 = "NA.USA.000.000.00000";
        placeRepository.save(new Place(id1));
        placeRepository.save(new Place(id2));
        placeRepository.save(new Place(id3));
        placeRepository.save(new Place(id4));

        assertEquals(4, placeRepository.findAll().size());
        List<Place> placelist = placeRepository.getAllCountriesFromContinent(value);
        assertEquals(3, placelist.size());

        assertEquals(placelist.get(0).getId(), id2);
        assertEquals(placelist.get(1).getId(), id3);
        assertEquals(placelist.get(2).getId(), id1);
    }

    @Test
    public void return_two_provinces_from_country(){
        String value = "EU.PL0";
        String id1 = "EU.PL0.MAL.000.00000";
        String id2 = "EU.PL0.DLS.000.00000";
        String id3 = "EU.PL0.WKP.000.00000";
        String id4 = "EU.GER.MUN.000.00000";
        String id5 = "EU.PL0.000.000.00000";

        placeRepository.save(new Place(id1));
        placeRepository.save(new Place(id2));
        placeRepository.save(new Place(id3));
        placeRepository.save(new Place(id4));
        placeRepository.save(new Place(id5));

        assertEquals(5, placeRepository.findAll().size());
        List<Place> placelist = placeRepository.getAllProvincesFromCountry(value);
        assertEquals(3, placelist.size());

        assertEquals(placelist.get(0).getId(), id2);
        assertEquals(placelist.get(1).getId(), id1);
        assertEquals(placelist.get(2).getId(), id3);
    }

    @Test
    public void return_two_regions_form_province(){
        String value = "EU.PL0.DLS";
        String id1 = "EU.PL0.DLS.000.00000";
        String id2 = "EU.PL0.DLS.REG.00000";
        String id3 = "EU.PL0.DLS.WRO.00000";
        String id4 = "EU.PL0.WKP.KRK.00000";

        placeRepository.save(new Place(id1));
        placeRepository.save(new Place(id2));
        placeRepository.save(new Place(id3));
        placeRepository.save(new Place(id4));

        assertEquals(4, placeRepository.findAll().size());
        List<Place> placelist = placeRepository.getRegionsFromProvince(value);
        assertEquals(2, placelist.size());
        assertEquals(placelist.get(0).getId(), id2);
        assertEquals(placelist.get(1).getId(), id3);
    }

    @Test
    public void return_three_places_form_region(){
        String value = "EU.PL0.DLS.WRO";
        String id1 = "EU.PL0.DLS.WRO.00000";
        String id2 = "EU.PL0.DLS.WRO.00002";
        String id3 = "EU.PL0.DLS.WRO.00001";
        String id4 = "EU.PL0.DLS.REG.00000";

        placeRepository.save(new Place(id1));
        placeRepository.save(new Place(id2));
        placeRepository.save(new Place(id3));
        placeRepository.save(new Place(id4));

        assertEquals(4, placeRepository.findAll().size());
        List<Place> placelist = placeRepository.getPlacesFromRegion(value);
        assertEquals(2, placelist.size());
        assertEquals(placelist.get(0).getId(), id3);
        assertEquals(placelist.get(1).getId(), id2);
    }

    @Test
    void test_getAllChildrenPlaces_for_continents() {
        String parentid = "EU";
        String id1 = "EU.PL0.DLS.WRO.00000";
        String id2 = "EU.PL0.MAL.KRK.00002";
        String id3 = "NA.USA.NY0.REG.00002";

        placeRepository.save(new Place(id1));
        placeRepository.save(new Place(id2));
        placeRepository.save(new Place(id3));

        List<Place> list = placeRepository.getAllChildrenPlaces(parentid);
        assertEquals(2, list.size());
        assertEquals(id1, list.get(0).getId());
        assertEquals(id2, list.get(1).getId());
    }

    @Test
    void test_getAllChildrenPlaces_for_subregion() {
        String parentid = "EU.PL0.DLS.WRO";
        String id1 = "EU.PL0.DLS.WRO.0000A";
        String id2 = "EU.PL0.DLS.WRO.0000B";
        String id3 = "EU.PL0.DLS.000.00000";

        placeRepository.save(new Place(id1));
        placeRepository.save(new Place(id2));
        placeRepository.save(new Place(id3));

        List<Place> list = placeRepository.getAllChildrenPlaces(parentid);
        assertEquals(2, list.size());
        assertEquals(id1, list.get(0).getId());
        assertEquals(id2, list.get(1).getId());
    }

    @Test
    void test_getAllChildrenPlaces_for_inappropriate_value() {
        String parentid = "EU.PL0.DLS.WRO.000C";
        String id1 = "EU.PL0.DLS.WRO.0000A";
        String id2 = "EU.PL0.DLS.WRO.0000B";
        String id3 = "EU.PL0.DLS.000.00000";

        placeRepository.save(new Place(id1));
        placeRepository.save(new Place(id2));
        placeRepository.save(new Place(id3));

        List<Place> list = placeRepository.getAllChildrenPlaces(parentid);
        assertEquals(0, list.size());
    }
}
