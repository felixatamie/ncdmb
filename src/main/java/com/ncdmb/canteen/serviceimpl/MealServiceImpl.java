package com.ncdmb.canteen.serviceimpl;

import com.ncdmb.canteen.dtos.request.MealRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Canteen;
import com.ncdmb.canteen.entity.Meal;
import com.ncdmb.canteen.iservice.MealService;
import com.ncdmb.canteen.repository.CanteenRepository;
import com.ncdmb.canteen.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final CanteenRepository canteenRepository;

    @Override
    public ResponseEntity<?> addMeal(MealRequestDto dto) {

        Optional<Canteen> canteenOptional = canteenRepository.findById(dto.getCanteenId());
        if (canteenOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().message("Canteen not found, operation unsuccessful").success(false).build());

        Optional<Meal> mealOptional = mealRepository.findByName(dto.getName().toLowerCase());
        if(mealOptional.isPresent())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Meal with exact name: " + dto.getName() + " already exist").build());

        Meal meal = new Meal();
        meal.setName(dto.getName());
        meal.setPrice(dto.getPrice());
        meal.setQuantity(dto.getQuantity());
        meal.setPortionOrUnit(dto.getPortionOrUnit());
        meal.setIsAvailable(dto.getIsActive());
        meal.setCanteen(canteenOptional.get());

        Meal result = mealRepository.save(meal);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<?> editMeal(MealRequestDto dto, int mealId) {
        Optional<Meal> mealOptional = mealRepository.findByIdAndCanteen_Id(mealId,dto.getCanteenId());
        if(mealOptional.isEmpty())
            return ResponseEntity.ok(OperationalResponse.builder().success(false).message("Meal not found: ").build());

        mealRepository.updateMeal(dto.getName(),dto.getPrice(),dto.getIsActive(),mealId);
        Optional<Meal> mealUpdated = mealRepository.findByIdAndCanteen_Id(mealId,dto.getCanteenId());
        return ResponseEntity.ok(mealUpdated.get());
    }

    @Override
    public List<Meal> getAllMeal(int canteenId) {
        return mealRepository.findByCanteen_Id(canteenId, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public ResponseEntity<?> getAGivenMeal(int mealId, int canteenId) {
        Optional<Meal> mealOptional = mealRepository.findByIdAndCanteen_Id(mealId,canteenId);
        if(mealOptional.isEmpty()) {
            OperationalResponse response = OperationalResponse.builder().success(false).message("Meal not found: ").build();
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(mealOptional.get());
    }

    @Override
    public OperationalResponse removeMeal(int mealId, int canteenId) {
        Optional<Meal> mealOptional = mealRepository.findByIdAndCanteen_Id(mealId,canteenId);
        if(mealOptional.isEmpty()) {
            return OperationalResponse.builder().success(false).message("Meal not found: ").build();
        }

        mealRepository.removeByIdAndCanteen_Id(mealId,canteenId);
        return  OperationalResponse.builder().success(true).message("Meal deleted successfully ").build();
    }
}
