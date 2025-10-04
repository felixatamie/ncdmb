package com.ncdmb.canteen.controllers;


import com.ncdmb.canteen.dtos.request.MealRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Meal;
import com.ncdmb.canteen.iservice.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meal")
@RequiredArgsConstructor

public class MealController {

    private final MealService mealService;

    @PostMapping("/create")
    public ResponseEntity<?> addMeal(@RequestBody MealRequestDto dto)
    {
        return ResponseEntity.ok(mealService.addMeal(dto));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editMeal(@PathVariable int id, @RequestBody MealRequestDto dto)
    {
        return mealService.editMeal(dto, id);
    }

    @GetMapping("/all/{canteenId}")
    public ResponseEntity<List<Meal>> getALlMeals(@PathVariable int canteenId)
    {
        return ResponseEntity.ok(mealService.getAllMeal(canteenId));
    }

    @GetMapping("/{id}/{canteenId}")
    public ResponseEntity<?> getAMealDetails(@PathVariable int id, @PathVariable int canteenId)
    {
        return mealService.getAGivenMeal(id, canteenId);
    }
    @DeleteMapping("/{id}/{canteenId}")
    public ResponseEntity<OperationalResponse> removeAMeal(@PathVariable int id, @PathVariable int canteenId)
    {
        return ResponseEntity.ok(mealService.removeMeal(id, canteenId));
    }

}
