package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class RatingServiceTests {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    private Rating rating1;
    private Rating rating2;

    @BeforeEach
    public void setUp() {
        rating1 = new Rating(1, "Moodys Rating", "Sand PRating", "Fitch Rating", 10);
        rating2 = new Rating(2, "Moodys Rating2", "Sand PRating2", "Fitch Rating2", 20);
    }

    @Test
    public void getAllTest() {
        List<Rating> ratings = Arrays.asList(rating1, rating2);
        when(ratingRepository.findAll()).thenReturn(ratings);
        List<Rating> result = ratingService.getAll();
        verify(ratingRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    public void getByIdTest() {
        when(ratingRepository.findById(rating1.getId())).thenReturn(Optional.ofNullable(rating1));
        ratingService.createRating(rating1);
        Rating result = ratingService.getById(rating1.getId());
        assertEquals(rating1, result);
        assertFalse(ObjectUtils.isEmpty(result));
        assertEquals(rating1.getMoodysRating(), result.getMoodysRating());
        assertEquals(rating1.getSandPRating(), result.getSandPRating());
        assertEquals(rating1.getFitchRating(), result.getFitchRating());
        assertEquals(rating1.getOrderNumber(), result.getOrderNumber());
    }

    @Test
    public void createRatingTest() {
        when(ratingRepository.save(rating1)).thenReturn(rating1);
        ratingService.createRating(rating1);
        verify(ratingRepository, times(1)).save(rating1);
    }

    @Test
    public void updateRatingTest() {
        when(ratingRepository.save(rating1)).thenReturn(rating1);
        ratingService.updateRating(rating1);
        verify(ratingRepository, times(1)).save(rating1);
    }

    @Test
    public void deleteRatingByIdTest() {
        when(ratingRepository.findById(rating1.getId())).thenReturn(Optional.ofNullable(rating1));
        ratingService.deleteRatingById(rating1.getId());
        verify(ratingRepository, times(1)).deleteById(rating1.getId());
    }
}
