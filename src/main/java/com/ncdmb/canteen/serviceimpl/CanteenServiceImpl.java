package com.ncdmb.canteen.serviceimpl;

import com.ncdmb.canteen.dtos.request.CanteenRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Canteen;
import com.ncdmb.canteen.iservice.CanteenService;
import com.ncdmb.canteen.repository.CanteenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor

public class CanteenServiceImpl implements CanteenService {

    private final CanteenRepository canteenRepository;
    @Override
    public OperationalResponse addCanteen(CanteenRequestDto dto) {

        Canteen canteen = new Canteen();
        canteen.setName(dto.getName());
        canteen.setEmail(dto.getEmail());
        canteen.setPhoneNumber(dto.getPhoneNumber());
        canteen.setCreatedAt(LocalDateTime.now());
        canteenRepository.save(canteen);
        return OperationalResponse.builder().success(true).message("Canteen created successfully").build();
    }

    @Override
    public List<Canteen> getAllCanteens() {
        return (List<Canteen>) canteenRepository.findAll();
    }
}
