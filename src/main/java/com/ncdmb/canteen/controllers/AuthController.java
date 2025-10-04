package com.ncdmb.canteen.controllers;

import com.ncdmb.canteen.CanteenApplication;
import com.ncdmb.canteen.configuration.JwtUtil;
import com.ncdmb.canteen.dtos.request.UserDto;
import com.ncdmb.canteen.dtos.response.JwtResponse;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.CanteenUser;
import com.ncdmb.canteen.entity.NCDMBUser;
import com.ncdmb.canteen.iservice.NCDMBStaffService;
import com.ncdmb.canteen.repository.CanteenUserRepository;
import com.ncdmb.canteen.repository.NCDMBUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final CanteenUserRepository canteenUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final NCDMBUserRepository ncdmbUserRepository;

    private  final NCDMBStaffService ncdmbStaffService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDto loginRequest) throws Exception {

        if(loginRequest.getUserType().equalsIgnoreCase("user"))
        {
            Optional<CanteenUser> userOptional = canteenUserRepository.findByUsername(loginRequest.getUsername());
            if(userOptional.isEmpty())
                return ResponseEntity.status(401).body("Unknown username");

            if (!passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPasswordHash())) {
                return ResponseEntity.status(401).body("incorrect password");
            }
            String token = JwtUtil.generateToken(loginRequest.getUsername(), "user", userOptional.get().getCanteen().getId());

            return ResponseEntity.ok(new JwtResponse(token));


        } else if (loginRequest.getUserType().equalsIgnoreCase("admin")) {
            Optional<NCDMBUser> ncdmbUserOptional = ncdmbUserRepository.findByUsername(loginRequest.getUsername());
            if(ncdmbUserOptional.isEmpty())
            {
                return ResponseEntity.status(401).body("Unknown username");
            }
            if (!passwordEncoder.matches(loginRequest.getPassword(), ncdmbUserOptional.get().getPasswordHash())) {
                return ResponseEntity.status(401).body("incorrect password");
            }

            String token = JwtUtil.generateToken(loginRequest.getUsername(), "admin",0);
            return ResponseEntity.ok(new JwtResponse(token));

        }

        return ResponseEntity.status(401).body("unknown usertype");
    }

    @PostMapping("/add/user")
    public ResponseEntity<OperationalResponse> addNCDMBUser(@RequestBody UserDto userDto)
    {
        return ResponseEntity.ok(ncdmbStaffService.addStaffUser(userDto));
    }

    public static void main(String[] args) {

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String result = encoder.encode("123456");
        System.out.println(result);


    }

}
