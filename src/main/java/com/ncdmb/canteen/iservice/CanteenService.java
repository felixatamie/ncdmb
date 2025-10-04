package com.ncdmb.canteen.iservice;

import com.ncdmb.canteen.dtos.request.CanteenRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Canteen;

import java.util.List;

public interface CanteenService {

    OperationalResponse addCanteen(CanteenRequestDto dto);
    List<Canteen> getAllCanteens ();
}
