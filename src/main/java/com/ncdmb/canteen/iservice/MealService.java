package com.ncdmb.canteen.iservice;

import com.ncdmb.canteen.dtos.request.MealRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Meal;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MealService {
   ResponseEntity<?> addMeal(MealRequestDto dto);
    ResponseEntity<?> editMeal(MealRequestDto dto, int mealId);
    List<Meal> getAllMeal(int canteenId);
    ResponseEntity<?> getAGivenMeal(int mealId, int canteenId);
    OperationalResponse removeMeal(int mealId, int canteenId);

}
