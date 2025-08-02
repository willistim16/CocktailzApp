package com.cocktailz.cocktailzclean.controller;

import com.cocktailz.cocktailzclean.entity.User;
import com.cocktailz.cocktailzclean.dto.RatingDto;
import com.cocktailz.cocktailzclean.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> rateCocktail(@RequestBody RatingDto ratingDto,
                                          @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ratingService.rate(user, ratingDto));
    }
}
