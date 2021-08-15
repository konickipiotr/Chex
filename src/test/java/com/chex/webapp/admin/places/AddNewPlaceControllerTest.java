package com.chex.webapp.admin.places;

import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser",roles={"ADMIN"})
@Transactional
class AddNewPlaceControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private PlaceRepository placeRepository;
    private PlaceNameRepository placeNameRepository;
    private PlaceDescriptionRepository placeDescriptionRepository;
    private CategoryRepository categoryRepository;
    private final String BASE_PATH =  "/admin/places/new";

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
    public AddNewPlaceControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository, CategoryRepository categoryRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.placeRepository = placeRepository;
        this.placeNameRepository = placeNameRepository;
        this.placeDescriptionRepository = placeDescriptionRepository;
        this.categoryRepository = categoryRepository;
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
    void when_continent_has_not_any_place_return_empty_country_list() throws Exception {
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, Object> model = mockMvc.perform(get(BASE_PATH + "/continent").param("continent", "EU"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/intermediateplaces"))
                .andExpect(model().attribute("placetype", "countries"))
                .andReturn().getModelAndView().getModel();

        //Object list = (List<Pmodel.get("list");
    }

}