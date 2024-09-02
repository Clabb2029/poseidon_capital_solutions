package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "Usertest", password = "passwordtest", roles = "USER")
public class RatingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    private Rating rating1;
    private Rating rating2;

    @BeforeEach
    public void setUp() {
        rating1 = new Rating(1, "Moodys Rating", "Sand PRating", "Fitch Rating", 10);
        rating2 = new Rating(2, "Moodys Rating2", "Sand PRating2", "Fitch Rating2", 20);    }

    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("username", "Usertest"))
                .andExpect(view().name("rating/list"));
    }

    @Test
    public void addRatingTest() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeExists("rating"));
    }

    @Test
    public void validateTest() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", String.valueOf(rating1.getMoodysRating()))
                        .param("sandPRating", String.valueOf(rating1.getSandPRating()))
                        .param("fitchRating", String.valueOf(rating1.getFitchRating()))
                        .param("orderNumber", String.valueOf(rating1.getOrderNumber()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
        verify(ratingService, times(1)).createRating(any(Rating.class));
    }

    @Test
    public void showUpdateFormTest() throws Exception {
        when(ratingService.getById(1)).thenReturn(rating1);
        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("rating/update"));
        verify(ratingService, times(1)).getById(1);
    }

    @Test
    public void showUpdateFormWhenNotFoundTest() throws Exception {
        when(ratingService.getById(1)).thenThrow(new IllegalArgumentException("No rating found."));
        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("rating/update"));
        verify(ratingService, times(1)).getById(1);
    }

    @Test
    public void updateRatingTest() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                        .param("moodysRating", String.valueOf(rating2.getMoodysRating()))
                        .param("sandPRating", String.valueOf(rating2.getSandPRating()))
                        .param("fitchRating", String.valueOf(rating2.getFitchRating()))
                        .param("orderNumber", String.valueOf(rating2.getOrderNumber()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    public void deleteRatingTest() throws Exception {
        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }
}
