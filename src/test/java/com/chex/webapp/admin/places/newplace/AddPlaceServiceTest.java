package com.chex.webapp.admin.places.newplace;

import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.*;
import com.chex.utils.Duo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddPlaceServiceTest {

    private MockMvc mockMvc;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private PlaceNameRepository placeNameRepository;
    @Mock
    private PlaceDescriptionRepository placeDescriptionRepository;

    @InjectMocks
    private AddPlaceService addPlaceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(addPlaceService).build();
    }

    @Test
    void get_continent_s_places() {
        PlaceType placeType = PlaceType.COUNTRY;
        String id = "EU";

        List<Place> list = new ArrayList<>(Arrays.asList(
                new Place("EU.POL.000.000.00000"),
                new Place("EU.GER.000.000.00000"),
                new Place("EU.ITL.000.000.00000")
        ));
        PlaceName pn1 = new PlaceName("EU.POL.000.000.00000", "Polska", "Poland");
        PlaceName pn2 = new PlaceName("EU.GER.000.000.00000", "Niemcy", "Germany");
        PlaceName pn3 = new PlaceName("EU.ITL.000.000.00000", "Włochy", "Italy");

        Mockito.when(placeRepository.getAllCountriesFromContinent(id)).thenReturn(list);
        Mockito.when(placeNameRepository.getById("EU.POL.000.000.00000")).thenReturn(pn1);
        Mockito.when(placeNameRepository.getById("EU.GER.000.000.00000")).thenReturn(pn2);
        Mockito.when(placeNameRepository.getById("EU.ITL.000.000.00000")).thenReturn(pn3);


        List<Duo<String>> listOfPlaces = addPlaceService.getListOfPlaces(id, placeType, "pl");

        assertEquals(3, listOfPlaces.size());
        assertEquals("EU.GER.000.000.00000", listOfPlaces.get(0).getKey());
        assertEquals("Niemcy", listOfPlaces.get(0).getValue());
        assertEquals("EU.POL.000.000.00000", listOfPlaces.get(1).getKey());
        assertEquals("Polska", listOfPlaces.get(1).getValue());
        assertEquals("EU.ITL.000.000.00000", listOfPlaces.get(2).getKey());
        assertEquals("Włochy", listOfPlaces.get(2).getValue());
    }

    @Test
    void get_country_s_places() {
        PlaceType placeType = PlaceType.PROVINCE;
        String id = "EU.POL";

        List<Place> list = new ArrayList<>(Arrays.asList(
                new Place("EU.POL.WKP.000.00000"),
                new Place("EU.POL.DLS.000.00000"),
                new Place("EU.POL.ZAC.000.00000")
        ));
        PlaceName pn1 = new PlaceName("EU.POL.WKP.000.00000", "Wielkopolskie", "Wielkopolskie");
        PlaceName pn2 = new PlaceName("EU.POL.DLS.000.00000", "Dolnośląskie", "Dolnośląskie");
        PlaceName pn3 = new PlaceName("EU.POL.ZAC.000.00000", "Zachodniopomorskie", "Zachodniopomorskie");

        Mockito.when(placeRepository.getAllProvincesFromCountry(id)).thenReturn(list);
        Mockito.when(placeNameRepository.getById("EU.POL.WKP.000.00000")).thenReturn(pn1);
        Mockito.when(placeNameRepository.getById("EU.POL.DLS.000.00000")).thenReturn(pn2);
        Mockito.when(placeNameRepository.getById("EU.POL.ZAC.000.00000")).thenReturn(pn3);

        List<Duo<String>> listOfPlaces = addPlaceService.getListOfPlaces(id, placeType, "pl");

        assertEquals(3, listOfPlaces.size());
        assertEquals("EU.POL.DLS.000.00000", listOfPlaces.get(0).getKey());
        assertEquals("Dolnośląskie", listOfPlaces.get(0).getValue());
        assertEquals("EU.POL.WKP.000.00000", listOfPlaces.get(1).getKey());
        assertEquals("Wielkopolskie", listOfPlaces.get(1).getValue());
        assertEquals("EU.POL.ZAC.000.00000", listOfPlaces.get(2).getKey());
        assertEquals("Zachodniopomorskie", listOfPlaces.get(2).getValue());
    }

    @Test
    void get_province_s_places() {
        PlaceType placeType = PlaceType.REGION;
        String id = "EU.POL.DLS";

        List<Place> list = new ArrayList<>(Arrays.asList(
                new Place("EU.POL.DLS.WRO.00000"),
                new Place("EU.POL.DLS.REG.00000"),
                new Place("EU.POL.DLS.KLD.00000")
        ));
        PlaceName pn1 = new PlaceName("EU.POL.DLS.REG.00000", "Region", "Region");
        PlaceName pn2 = new PlaceName("EU.POL.DLS.WRO.00000", "Wrocław", "Wrocław");
        PlaceName pn3 = new PlaceName("EU.POL.DLS.KLD.00000", "Kłodzko", "Kłodzko");

        Mockito.when(placeRepository.getRegionsFromProvince(id)).thenReturn(list);
        Mockito.when(placeNameRepository.getById("EU.POL.DLS.WRO.00000")).thenReturn(pn2);
        Mockito.when(placeNameRepository.getById("EU.POL.DLS.REG.00000")).thenReturn(pn1);
        Mockito.when(placeNameRepository.getById("EU.POL.DLS.KLD.00000")).thenReturn(pn3);


        List<Duo<String>> listOfPlaces = addPlaceService.getListOfPlaces(id, placeType, "pl");

        assertEquals(3, listOfPlaces.size());
        assertEquals("EU.POL.DLS.REG.00000", listOfPlaces.get(0).getKey());
        assertEquals("Region", listOfPlaces.get(0).getValue());

        assertEquals("EU.POL.DLS.KLD.00000", listOfPlaces.get(1).getKey());
        assertEquals("Kłodzko", listOfPlaces.get(1).getValue());

        assertEquals("EU.POL.DLS.WRO.00000", listOfPlaces.get(2).getKey());
        assertEquals("Wrocław", listOfPlaces.get(2).getValue());
    }
}
