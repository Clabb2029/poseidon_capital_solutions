package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getAll() {
        return ratingRepository.findAll();
    }

    public Rating getById(Integer id) {
        return ratingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No rating found with id: " + id));
    }

    public void createRating(Rating rating) {
        ratingRepository.save(rating);
    }

    public void updateRating(Rating rating) {
        ratingRepository.save(rating);
    }

    public void deleteRatingById(Integer id) {
        ratingRepository.deleteById(id);
    }
}
