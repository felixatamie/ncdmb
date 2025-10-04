package com.ncdmb.canteen.serviceimpl;

import com.ncdmb.canteen.dtos.request.CanteenUserRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Canteen;
import com.ncdmb.canteen.entity.CanteenUser;
import com.ncdmb.canteen.entity.Role;
import com.ncdmb.canteen.iservice.CanteenUserService;
import com.ncdmb.canteen.repository.CanteenRepository;
import com.ncdmb.canteen.repository.CanteenUserRepository;
import com.ncdmb.canteen.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CanteenUserServiceImpl implements CanteenUserService {

    private final CanteenRepository canteenRepository;
    private final RoleRepository roleRepository;
    private final CanteenUserRepository canteenUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OperationalResponse addUser(CanteenUserRequestDto dto) {

        Optional<Canteen> canteenOptional = canteenRepository.findById(dto.getCanteenId());
        if (canteenOptional.isEmpty())
            return OperationalResponse.builder().message("Canteen not found, operation unsuccessful").success(false).build();

        Optional<Role> roleOptional = roleRepository.findById(dto.getRoleId());
        if(roleOptional.isEmpty())
            return OperationalResponse.builder().message("Role not found, operation unsuccessful").success(false).build();

        CanteenUser canteenUser = new CanteenUser();
        canteenUser.setCanteen(canteenOptional.get());
        canteenUser.setRole(roleOptional.get());
        canteenUser.setUsername(dto.getUsername());
        canteenUser.setIsActive(dto.getIsActive());
        canteenUser.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        canteenUser.setName(dto.getName());
        canteenUser.setPhone(dto.getPhone());
        canteenUserRepository.save(canteenUser);

        return OperationalResponse.builder().message("Your username is '"+dto.getUsername()+"' password is '"+dto.getPassword()+"' , keep them safe").success(true).build();
    }

    @Override
    public OperationalResponse removeUser(int canteenId, int userId) {

        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        if (canteenOptional.isEmpty())
            return OperationalResponse.builder().message("Canteen not found, operation unsuccessful").success(false).build();

        Optional<CanteenUser> canteenUserOptional = canteenUserRepository.findById(userId);
        if (canteenUserOptional.isEmpty())
            return OperationalResponse.builder().message("Canteen User not found, operation unsuccessful").success(false).build();

        canteenUserRepository.deleteByIdAndCanteen_Id(userId,canteenId);
        return OperationalResponse.builder().message("operation successfully performed").success(true).build();
    }

    @Override
    public List<CanteenUser> allCanteenUsers(int canteenId) {

        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        if (canteenOptional.isEmpty())
            return List.of();

        return canteenUserRepository.findByCanteen_Id(canteenId);
    }

    @Override
    public OperationalResponse inactivateCanteenUser(int canteenId, int userId) {

        Optional<Canteen> canteenOptional = canteenRepository.findById(canteenId);
        if (canteenOptional.isEmpty())
            return OperationalResponse.builder().message("Canteen not found, operation unsuccessful").success(false).build();

        Optional<CanteenUser> canteenUserOptional = canteenUserRepository.findById(userId);
        if (canteenUserOptional.isEmpty())
            return OperationalResponse.builder().message("Canteen User not found, operation unsuccessful").success(false).build();
        canteenUserRepository.updateIsActiveByIdAndCanteenId(userId,canteenId,false);
        return OperationalResponse.builder().message("operation successfully performed").success(true).build();
    }
}
