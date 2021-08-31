package com.chex.api.place;

import com.chex.modules.Coords;
import com.chex.modules.places.Place;
import com.chex.modules.places.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChexPlaceServiceTest {

    private PlaceRepository placeRepository;
    private ChexPlaceService chexPlaceService;

    Place panoramaRaclawicka = new Place("EU.POL.DLS.WRO.00001");
    Place pomnikJuliuszaSlowackiego = new Place("EU.POL.DLS.WRO.00002");
    Place lionStatue = new Place("EU.POL.DLS.WRO.00003");

    @Autowired
    public ChexPlaceServiceTest(PlaceRepository placeRepository, ChexPlaceService chexPlaceService) {
        this.placeRepository = placeRepository;
        this.chexPlaceService = chexPlaceService;
    }

    @BeforeEach
    void setUp() {
        this.placeRepository.deleteAll();

        Place europa = new Place("EU.000.000.000.00000");
        Place northAmerica = new Place("NA.000.000.000.00000");

        Place poland = new Place("EU.POL.000.000.00000");
        Place germany = new Place("EU.GER.000.000.00000");

        Place dls = new Place("EU.POL.DLS.000.00000");
        Place mlp = new Place("EU.GER.MLP.000.00000");

        Place reg = new Place("EU.POL.DLS.REG.00000");
        Place WRO = new Place("EU.POL.DLS.WRO.00000");

        panoramaRaclawicka.setLatitude(51.11017356874449);
        panoramaRaclawicka.setLongitude(17.044386939744353);
        panoramaRaclawicka.setRadius(60);
        panoramaRaclawicka.setPoints(50);

        pomnikJuliuszaSlowackiego.setLatitude(51.109353047586225);
        pomnikJuliuszaSlowackiego.setLongitude(17.04519811419831);
        pomnikJuliuszaSlowackiego.setRadius(60);
        pomnikJuliuszaSlowackiego.setPoints(50);

        lionStatue.setLatitude(51.11109957040286);
        lionStatue.setLongitude(17.022283162620134);
        lionStatue.setRadius(30);
        lionStatue.setPoints(50);

        this.placeRepository.saveAll(Arrays.asList(europa, northAmerica, poland, germany, dls, mlp, reg, WRO, panoramaRaclawicka, pomnikJuliuszaSlowackiego, lionStatue));
    }


    @Test
    void Nether_of_places_in_range() {
        Coords odraRiver = new Coords(51.111899534841235, 17.04601676487765);

        List<Place> places = this.chexPlaceService.filterPlace(odraRiver);

        assertTrue(places.isEmpty());
    }

    @Test
    void one_place_in_the_range() {
        Coords street = new Coords(51.11065207427421, 17.044421721215873);

        List<Place> places = this.chexPlaceService.filterPlace(street);

        assertEquals(1, places.size());
        assertEquals("EU.POL.DLS.WRO.00001", places.get(0).getId());
    }

    @Test
    void two_place_in_the_range() {
        Coords park = new Coords(51.10969087447883, 17.04473464530148);

        List<Place> places = this.chexPlaceService.filterPlace(park);

        assertEquals(2, places.size());
        assertTrue(places.contains(panoramaRaclawicka));
        assertTrue(places.contains(pomnikJuliuszaSlowackiego));
    }
}
