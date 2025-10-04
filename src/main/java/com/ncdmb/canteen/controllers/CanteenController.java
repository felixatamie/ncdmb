package com.ncdmb.canteen.controllers;


import com.ncdmb.canteen.dtos.request.CanteenRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Canteen;
import com.ncdmb.canteen.serviceimpl.CanteenServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/canteen")
@RequiredArgsConstructor
public class CanteenController {

    private final CanteenServiceImpl canteenService;

    @PostMapping("/create")
    public ResponseEntity<OperationalResponse> addCanteen(@RequestBody CanteenRequestDto dto)
    {
        return ResponseEntity.ok(canteenService.addCanteen(dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Canteen>> allCanteen()
    {
        return ResponseEntity.ok(canteenService.getAllCanteens());
    }
}
