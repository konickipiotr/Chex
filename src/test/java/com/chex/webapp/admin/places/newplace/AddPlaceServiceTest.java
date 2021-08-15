package com.chex.webapp.admin.places.newplace;

import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AddPlaceServiceTest {

    private PlaceRepository placeRepository;
    private PlaceNameRepository placeNameRepository;
    private PlaceDescriptionRepository placeDescriptionRepository;
    private CategoryRepository categoryRepository;

    private AddPlaceService newPlaceService;

    private Category cat1 = new Category("kontynetn", "continent");
    private Category cat2 = new Category("państwo", "country");
    private Category cat3 = new Category("prowincja", "province");
    private Category cat4 = new Category("Miejscowość","city");
    private Category cat5 = new Category("region", "region");

    private Place p1 = new Place("EU0000000000000");
    private PlaceName pn1;
    private PlaceDescription pd1;
    private Place p2 = new Place("AN0000000000000");
    private PlaceName pn2;
    private PlaceDescription pd2;

    @Autowired
    public AddPlaceServiceTest(PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository, CategoryRepository categoryRepository, AddPlaceService newPlaceService) {
        this.placeRepository = placeRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
        this.categoryRepository = categoryRepository;
        this.newPlaceService = newPlaceService;
    }

    @BeforeEach
    void setUp() {

        this.placeRepository.deleteAll();
        this.placeNameRepository.deleteAll();
        this.placeDescriptionRepository.deleteAll();

        this.categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5));
        p1.setCategory(cat1.getId());
        p2.setCategory(cat1.getId());
        this.placeRepository.saveAll(Arrays.asList(p1, p2));

        pn1 = new PlaceName(p1.getId(), "Europa", "Europe");
        pn2 = new PlaceName(p2.getId(), "Ameryka Pónocna", "North America");
        this.placeNameRepository.saveAll(Arrays.asList(pn1, pn2));

        pd1 = new PlaceDescription(p1.getId(), "pies", "koń");
        pd2 = new PlaceDescription(p2.getId(), "zielony", "niebieski");
    }

    @Test
    void add_general_place_in_proper_way() {
        PlaceForm pf = new PlaceForm();
        pf.setPrefix("EU");
        pf.setSufix("00000000000");
        pf.setPlaceid("PL0");
        pf.setNamePl("Polska");
        pf.setNameEng("Poland");
        pf.setDescriptionEng("Country");
        pf.setDescriptionPl("Kraj");

        boolean added = this.newPlaceService.addNewGeneralPlace(pf);
        String expectedId = "EUPL000000000000";

        assertTrue(added);
        Place place = this.placeRepository.getById(expectedId);
        PlaceName placeName = this.placeNameRepository.getById(place.getId());
        PlaceDescription description = this.placeDescriptionRepository.getById(place.getId());

        assertEquals(expectedId, place.getId());
        assertEquals(10000, place.getLatitude());
        assertEquals(10000, place.getLongitude());
        assertEquals(0, place.getRadius());
        assertEquals(0, place.getRating());
        assertEquals(cat2.getId(), place.getCategory());

        assertEquals("Polska", placeName.getPl());
        assertEquals("Poland", placeName.getEng());

        assertEquals("Country", description.getEng());
        assertEquals("Kraj", description.getPl());

    }
}