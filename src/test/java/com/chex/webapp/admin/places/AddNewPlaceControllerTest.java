package com.chex.webapp.admin.places;

import com.chex.modules.category.Category;
import com.chex.modules.category.CategoryRepository;
import com.chex.modules.places.*;
import com.chex.utils.Duo;
import com.chex.webapp.admin.places.newplace.PlaceForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WithMockUser(username="testuser",roles={"ADMIN"})
@Transactional
class AddNewPlaceControllerTest {

    private final MockMvc mockMvc;
    private final PlaceRepository placeRepository;
    private final PlaceNameRepository placeNameRepository;
    private final PlaceDescriptionRepository placeDescriptionRepository;
    private final CategoryRepository categoryRepository;

    private final Category cat1 = new Category("kontynetn", "continent");
    private final Category cat2 = new Category("państwo", "country");
    private final Category cat3 = new Category("prowincja", "province");
    private final Category cat4 = new Category("Miejscowość","city");
    private final Category cat5 = new Category("region", "region");

    private final Place p1 = new Place("EU0000000000000");
    private final Place p2 = new Place("AN0000000000000");

    @Autowired
    public AddNewPlaceControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, PlaceRepository placeRepository, PlaceNameRepository placeNameRepository, PlaceDescriptionRepository placeDescriptionRepository, CategoryRepository categoryRepository) {
        this.mockMvc = mockMvc;
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
        this.categoryRepository.deleteAll();

        this.categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5));
        p1.setCategory(cat1.getId());
        p2.setCategory(cat1.getId());
        this.placeRepository.saveAll(Arrays.asList(p1, p2));

        PlaceName pn1 = new PlaceName(p1.getId(), "Europa", "Europe");
        PlaceName pn2 = new PlaceName(p2.getId(), "Ameryka Pónocna", "North America");
        this.placeNameRepository.saveAll(Arrays.asList(pn1, pn2));

//        PlaceDescription pd1 = new PlaceDescription(p1.getId(), "pies", "koń");
//        PlaceDescription pd2 = new PlaceDescription(p2.getId(), "zielony", "niebieski");
    }

    @Test
    void integate_test_full_path() throws Exception {
        String BASE_PATH = "/admin/places/new";
        Map<String, Object> model = mockMvc.perform(get(BASE_PATH + "/continent").param("continent", "EU"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andExpect(model().attribute("placetype", PlaceType.COUNTRY))
                .andExpect(model().attribute("placeForm", hasProperty("prefix", is("EU"))))
                .andExpect(model().attribute("placeForm", hasProperty("suffix", is(".000.000.00000"))))
                .andExpect(model().attribute("placeForm", hasProperty("subplace", is(true))))
                .andReturn().getModelAndView().getModel();

        List<Duo<String>> pNames = (List<Duo<String>>) model.get("list");
        assertEquals(0, pNames.size());

        /*
        Add country
         */

        PlaceForm pf = new PlaceForm("EU", ".000.000.00000");
        pf.setPlaceid("POL");
        pf.setNamePl("Polska");
        pf.setNameEng("Poland");
        pf.setDescriptionPl("miejsce");
        pf.setDescriptionEng("place");
        pf.setPoints(10);
        pf.setDifficultylevel(1);
        pf.setCategory(cat2.getId());

        String polandID = pf.createId();

        model = mockMvc.perform(post(BASE_PATH + "/" + PlaceType.COUNTRY.name())
                .flashAttr("placeForm", pf))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andReturn().getModelAndView().getModel();

        Place poland = this.placeRepository.getById(polandID);
        PlaceName polandName = this.placeNameRepository.getById(polandID);
        PlaceDescription polandDesc = this.placeDescriptionRepository.getById(polandID);

        assertEquals(polandID, poland.getId());
        assertEquals(cat2.getId(), poland.getCategory());
        assertEquals(10000, poland.getLongitude());
        assertEquals(10000, poland.getLatitude());
        assertEquals(0, poland.getRadius());
        assertEquals(0, poland.getRating());
        assertEquals(1, poland.getDifficultylevel());
        assertNull(poland.getAchievements());

        assertEquals(pf.getNameEng(), polandName.getEng());
        assertEquals(pf.getNamePl(), polandName.getPl());

        assertEquals(pf.getDescriptionEng(), polandDesc.getEng());
        assertEquals(pf.getDescriptionPl(), polandDesc.getPl());

        pNames = (List<Duo<String>>) model.get("list");
        assertEquals(1, pNames.size());
        assertEquals(polandID, pNames.get(0).getKey());
        assertEquals("Polska", pNames.get(0).getValue());


        model = mockMvc.perform(get(BASE_PATH + "/" + PlaceType.COUNTRY.name()).param("selectedPlace", polandID))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andExpect(model().attribute("placetype", PlaceType.PROVINCE))
                .andExpect(model().attribute("placeForm", hasProperty("prefix", is("EU.POL"))))
                .andExpect(model().attribute("placeForm", hasProperty("suffix", is(".000.00000"))))
                .andExpect(model().attribute("placeForm", hasProperty("subplace", is(true))))
                .andReturn().getModelAndView().getModel();

        pNames = (List<Duo<String>>) model.get("list");
        assertEquals(0, pNames.size());


        pf = new PlaceForm("EU.POL", ".000.00000");
        pf.setPlaceid("DLS");
        pf.setNamePl("Dolnyśląk");
        pf.setNameEng("Dolnyslas");
        pf.setDescriptionPl("miejsce");
        pf.setDescriptionEng("place");
        pf.setPoints(20);
        pf.setDifficultylevel(1);
        pf.setCategory(cat3.getId());

        String dlsId = pf.createId();

        model = mockMvc.perform(post(BASE_PATH + "/" + PlaceType.PROVINCE.name())
                .flashAttr("placeForm", pf))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andReturn().getModelAndView().getModel();

        Place dls = this.placeRepository.getById(dlsId);
        PlaceName dlsName = this.placeNameRepository.getById(dlsId);
        PlaceDescription dlsDesc = this.placeDescriptionRepository.getById(dlsId);

        assertEquals(dlsId, dls.getId());
        assertEquals(cat3.getId(), dls.getCategory());
        assertEquals(10000, dls.getLongitude());
        assertEquals(10000, dls.getLatitude());
        assertEquals(0, dls.getRadius());
        assertEquals(0, dls.getRating());
        assertEquals(1, dls.getDifficultylevel());
        assertNull(dls.getAchievements());

        assertEquals(pf.getNameEng(), dlsName.getEng());
        assertEquals(pf.getNamePl(), dlsName.getPl());

        assertEquals(pf.getDescriptionEng(), dlsDesc.getEng());
        assertEquals(pf.getDescriptionPl(), dlsDesc.getPl());

        pNames = (List<Duo<String>>) model.get("list");
        assertEquals(1, pNames.size());
        assertEquals(dlsId, pNames.get(0).getKey());
        assertEquals("Dolnyśląk", pNames.get(0).getValue());

        //*************************************

        model = mockMvc.perform(get(BASE_PATH + "/" + PlaceType.PROVINCE.name()).param("selectedPlace", dlsId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andExpect(model().attribute("placetype", PlaceType.REGION))
                .andExpect(model().attribute("placeForm", hasProperty("prefix", is("EU.POL.DLS"))))
                .andExpect(model().attribute("placeForm", hasProperty("suffix", is(".00000"))))
                .andExpect(model().attribute("placeForm", hasProperty("subplace", is(true))))
                .andReturn().getModelAndView().getModel();

        pNames = (List<Duo<String>>) model.get("list");
        assertEquals(0, pNames.size());

        pf = new PlaceForm("EU.POL.DLS", ".00000");
        pf.setPlaceid("WRO");
        pf.setNamePl("Wrocław");
        pf.setSameAsFirst(true);
        pf.setDescriptionPl("miejsce");
        pf.setDescriptionEng("place");
        pf.setPoints(30);
        pf.setDifficultylevel(1);
        pf.setCategory(cat4.getId());

        String wroId = pf.createId();

        model = mockMvc.perform(post(BASE_PATH + "/" + PlaceType.REGION.name())
                .flashAttr("placeForm", pf))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andReturn().getModelAndView().getModel();

        Place wro = this.placeRepository.getById(wroId);
        PlaceName wroName = this.placeNameRepository.getById(wroId);
        PlaceDescription wroDesc = this.placeDescriptionRepository.getById(wroId);

        assertEquals(wroId, wro.getId());
        assertEquals(cat4.getId(), wro.getCategory());
        assertEquals(10000, wro.getLongitude());
        assertEquals(10000, wro.getLatitude());
        assertEquals(0, wro.getRadius());
        assertEquals(0, wro.getRating());
        assertEquals(1, wro.getDifficultylevel());
        assertNull(dls.getAchievements());

        assertEquals(pf.getNamePl(), wroName.getEng());
        assertEquals(pf.getNamePl(), wroName.getPl());

        assertEquals(pf.getDescriptionEng(), wroDesc.getEng());
        assertEquals(pf.getDescriptionPl(), wroDesc.getPl());

        pNames = (List<Duo<String>>) model.get("list");
        assertEquals(1, pNames.size());
        assertEquals(wroId, pNames.get(0).getKey());
        assertEquals("Wrocław", pNames.get(0).getValue());

        //*************************************
        model = mockMvc.perform(get(BASE_PATH + "/" + PlaceType.REGION.name()).param("selectedPlace", wroId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andExpect(model().attribute("placetype", PlaceType.OTHER))
                .andExpect(model().attribute("placeForm", hasProperty("prefix", is("EU.POL.DLS.WRO"))))
                .andExpect(model().attribute("placeForm", hasProperty("suffix", is(""))))
                .andExpect(model().attribute("placeForm", hasProperty("subplace", is(false))))
                .andReturn().getModelAndView().getModel();

        pNames = (List<Duo<String>>) model.get("list");
        assertEquals(0, pNames.size());

        pf = new PlaceForm("EU.POL.DLS.WRO", "");
        pf.setPlaceid("00001");
        pf.setNamePl("Rynek");
        pf.setNameEng("Marketplace");
        pf.setDescriptionPl("miejsce");
        pf.setDescriptionEng("place");
        pf.setPoints(30);
        pf.setDifficultylevel(1);
        pf.setCategory(null);
        pf.setLatitude(51.110287);
        pf.setLongitude(17.032092);
        pf.setRadius(200);

        String placeId = pf.createId();

        model = mockMvc.perform(post(BASE_PATH + "/" + PlaceType.OTHER.name())
                .flashAttr("placeForm", pf))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/places/newplace"))
                .andReturn().getModelAndView().getModel();

        Place pl = this.placeRepository.getById(placeId);
        PlaceName plName = this.placeNameRepository.getById(placeId);
        PlaceDescription plDesc = this.placeDescriptionRepository.getById(placeId);

        assertEquals(placeId, pl.getId());
        assertNull(pl.getCategory());
        assertEquals(pf.getLongitude(), pl.getLongitude());
        assertEquals(pf.getLatitude(), pl.getLatitude());
        assertEquals(pf.getRadius(), pl.getRadius());
        assertEquals(0, pl.getRating());
        assertEquals(1, pl.getDifficultylevel());
        assertNull(pl.getAchievements());

        assertEquals(pf.getNameEng(), plName.getEng());
        assertEquals(pf.getNamePl(), plName.getPl());

        assertEquals(pf.getDescriptionEng(), plDesc.getEng());
        assertEquals(pf.getDescriptionPl(), plDesc.getPl());

        pNames = (List<Duo<String>>) model.get("list");
        assertEquals(0, pNames.size());
    }

}