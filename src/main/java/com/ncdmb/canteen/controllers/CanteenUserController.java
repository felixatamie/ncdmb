package com.ncdmb.canteen.controllers;

import com.ncdmb.canteen.dtos.request.CanteenUserRequestDto;
import com.ncdmb.canteen.dtos.request.MealRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.CanteenUser;
import com.ncdmb.canteen.entity.Meal;
import com.ncdmb.canteen.iservice.CanteenUserService;
import com.ncdmb.canteen.iservice.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/canteen-owner")
@RequiredArgsConstructor
public class CanteenUserController {

    private final CanteenUserService canteenUserService;
    private final MealService mealService;

    @PostMapping("/create-user")
    public ResponseEntity<OperationalResponse> addUser(@RequestBody CanteenUserRequestDto dto)
    {
        return ResponseEntity.ok(canteenUserService.addUser(dto));
    }

    @DeleteMapping("/user/{canteenId}/{userId}")
    public ResponseEntity<OperationalResponse> removeUserFromACanteen(@PathVariable int canteenId, @PathVariable int userId)
    {
        return ResponseEntity.ok(canteenUserService.removeUser(canteenId,userId));
    }

    @GetMapping("/all-users/{canteenId}")
    public ResponseEntity<List<CanteenUser>> allUsersPerCanteen(@PathVariable int canteenId)
    {
        return ResponseEntity.ok(canteenUserService.allCanteenUsers(canteenId));
    }
    @PutMapping("/deactivate-user/{canteenId}/{userId}")
    public ResponseEntity<OperationalResponse> deactivateUser(@PathVariable int canteenId, @PathVariable int userId)
    {
        return ResponseEntity.ok(canteenUserService.inactivateCanteenUser(canteenId,userId));
    }

    @PostMapping("/meal/create")
    public ResponseEntity<?> addMeal(@RequestBody MealRequestDto dto)
    {
        return ResponseEntity.ok(mealService.addMeal(dto));
    }

    @PostMapping("/meal/edit/{id}")
    public ResponseEntity<?> editMeal(@PathVariable int id, @RequestBody MealRequestDto dto)
    {
        return mealService.editMeal(dto, id);
    }

    @GetMapping("/meal/all/{canteenId}")
    public ResponseEntity<List<Meal>> getALlMeals(@PathVariable int canteenId)
    {
        return ResponseEntity.ok(mealService.getAllMeal(canteenId));
    }

    @GetMapping("/meal/{id}/{canteenId}")
    public ResponseEntity<?> getAMealDetails(@PathVariable int id, @PathVariable int canteenId)
    {
        return mealService.getAGivenMeal(id, canteenId);
    }
    @DeleteMapping("meal/{id}/{canteenId}")
    public ResponseEntity<OperationalResponse> removeAMeal(@PathVariable int id, @PathVariable int canteenId)
    {
        return ResponseEntity.ok(mealService.removeMeal(id, canteenId));
    }

}
