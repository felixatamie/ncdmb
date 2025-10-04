package com.ncdmb.canteen.controllers;

import com.ncdmb.canteen.dtos.request.CadreRequestDto;
import com.ncdmb.canteen.dtos.request.CanteenRequestDto;
import com.ncdmb.canteen.dtos.request.NCDMBStaffRequestDto;
import com.ncdmb.canteen.dtos.request.UserDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Cadre;
import com.ncdmb.canteen.entity.Canteen;
import com.ncdmb.canteen.entity.NCDMBStaff;
import com.ncdmb.canteen.iservice.CadreService;
import com.ncdmb.canteen.iservice.NCDMBStaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ncdmb")
@RequiredArgsConstructor

public class NCDMBStaffController {

    private final NCDMBStaffService ncdmbStaffService;
    private final CadreService cadreService;

    @PostMapping("/create/canteen") //need
    public ResponseEntity<OperationalResponse> addCanteen(@RequestBody CanteenRequestDto dto)
    {
        return ResponseEntity.ok(ncdmbStaffService.addCanteen(dto));
    }

    @GetMapping("/all/canteen") //need
    public ResponseEntity<List<Canteen>> allCanteen()
    {
        return ResponseEntity.ok(ncdmbStaffService.getAllCanteens());
    }

    @PostMapping("/create/staff")
    public ResponseEntity<OperationalResponse> addStaff(@RequestBody NCDMBStaffRequestDto dto)
    {
        return ResponseEntity.ok(ncdmbStaffService.addStaff(dto));
    }

    @PutMapping("/alter/staff/{id}/{status}")
    public ResponseEntity<OperationalResponse> alterStaffStatus(@PathVariable int id, @PathVariable String status)
    {
        return ResponseEntity.ok(ncdmbStaffService.alterStaffStatus(id, status));
    }

    @GetMapping("/all/staff")
    public ResponseEntity<List<NCDMBStaff>> getAllStaff()
    {
        return ResponseEntity.ok(ncdmbStaffService.allStaff());
    }

    @PostMapping("/edit/cadre/{id}")
    public ResponseEntity<OperationalResponse> editCadre(@PathVariable int id, @RequestBody CadreRequestDto dto)
    {
        return ResponseEntity.ok(cadreService.editCadre(id, dto));
    }

    @GetMapping("/all/cadre") //need
    public ResponseEntity<List<Cadre>> getAll()
    {
        return ResponseEntity.ok(cadreService.getAllCadres());
    }

}
