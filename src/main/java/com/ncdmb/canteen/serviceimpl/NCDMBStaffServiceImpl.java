package com.ncdmb.canteen.serviceimpl;

import com.ncdmb.canteen.dtos.request.CanteenRequestDto;
import com.ncdmb.canteen.dtos.request.NCDMBStaffRequestDto;
import com.ncdmb.canteen.dtos.request.UserDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Cadre;
import com.ncdmb.canteen.entity.Canteen;
import com.ncdmb.canteen.entity.NCDMBStaff;
import com.ncdmb.canteen.entity.NCDMBUser;
import com.ncdmb.canteen.iservice.CanteenService;
import com.ncdmb.canteen.iservice.NCDMBStaffService;
import com.ncdmb.canteen.repository.CadreRepository;
import com.ncdmb.canteen.repository.CanteenRepository;
import com.ncdmb.canteen.repository.NCDMBStaffRepository;
import com.ncdmb.canteen.repository.NCDMBUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class NCDMBStaffServiceImpl implements NCDMBStaffService {

    private final NCDMBStaffRepository ncdmbStaffRepository;
    private final CadreRepository cadreRepository;
    private final CanteenRepository canteenRepository;
    private  final NCDMBUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public OperationalResponse addStaff(NCDMBStaffRequestDto dto) {

        Optional<NCDMBStaff> localContentStaffOptional = ncdmbStaffRepository.findByStaffId(dto.getStaffId());
        if (localContentStaffOptional.isPresent())
            return OperationalResponse.builder().message("staff already exist, operation unsuccessful").success(false).build();

        Optional<Cadre> cadreOptional = cadreRepository.findById(dto.getCadreId());
        if(cadreOptional.isEmpty())
            return OperationalResponse.builder().message("Cadre not found, operation unsuccessful").success(false).build();

        NCDMBStaff staff = new NCDMBStaff();
        staff.setStaffId(dto.getStaffId());
        staff.setCadre(cadreOptional.get());
        staff.setName(dto.getName());
        staff.setPhone(dto.getPhone());
        staff.setEmail(dto.getEmail());
        staff.setStatus(NCDMBStaff.Status.valueOf(dto.getStatus()));
        ncdmbStaffRepository.save(staff);
        return OperationalResponse.builder().success(true).message("staff with id: " + dto.getStaffId() +" successfully added").build();
    }

    @Override
    public OperationalResponse alterStaffStatus(int staffId, String status) {

        Optional<NCDMBStaff> localContentStaffOptional = ncdmbStaffRepository.findByStaffId(status);
        if (localContentStaffOptional.isEmpty())
            return OperationalResponse.builder().message("staff not found, operation unsuccessful").success(false).build();
        ncdmbStaffRepository.updateStatusById(staffId, NCDMBStaff.Status.valueOf(status));

        return OperationalResponse.builder().message("Staff status changed to '"+ status + "' successfully").success(true).build();
    }

    @Override
    public List<NCDMBStaff> allStaff() {
        return (List<NCDMBStaff>) ncdmbStaffRepository.findAll();
    }

    @Override
    public OperationalResponse addStaffUser(UserDto dto) {
        NCDMBUser user = new NCDMBUser();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return OperationalResponse.builder().message("ncdmb user added successfully").success(true).build();
    }

    @Override
    public OperationalResponse addCanteen(CanteenRequestDto dto) {

        Canteen canteen = new Canteen();
        canteen.setName(dto.getName());
        canteen.setCreatedAt(LocalDateTime.now());
        canteenRepository.save(canteen);
        return OperationalResponse.builder().success(true).message("Canteen created successfully").build();
    }

    @Override
    public List<Canteen> getAllCanteens() {
        return (List<Canteen>) canteenRepository.findAll();
    }
}
