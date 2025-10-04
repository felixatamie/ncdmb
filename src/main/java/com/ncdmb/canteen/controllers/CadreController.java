package com.ncdmb.canteen.controllers;


import com.ncdmb.canteen.dtos.request.CadreRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Cadre;
import com.ncdmb.canteen.iservice.CadreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cadre")
@RequiredArgsConstructor
public class CadreController {

    private final CadreService cadreService;

    @PostMapping("/create")
    public ResponseEntity<OperationalResponse> addCadre(@RequestBody CadreRequestDto dto)
    {
        return ResponseEntity.ok(cadreService.addCadre(dto));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<OperationalResponse> editCadre(@PathVariable int id, @RequestBody CadreRequestDto dto)
    {
        return ResponseEntity.ok(cadreService.editCadre(id, dto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cadre>> getAll()
    {
        return ResponseEntity.ok(cadreService.getAllCadres());
    }
}
