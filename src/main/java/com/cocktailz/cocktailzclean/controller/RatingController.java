package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.entity.Rating;
import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.dto.RatingDto;
import com.cocktailz.cocktailzclean.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@PreAuthorize("isAuthenticated()")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public ResponseEntity<List<RatingDto>> getRatings(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ratingService.getRatings(user));
    }

    @PostMapping
    public ResponseEntity<Rating> rateCocktail(@RequestBody RatingDto ratingDto,
                                               @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ratingService.rate(user, ratingDto));
    }
}
