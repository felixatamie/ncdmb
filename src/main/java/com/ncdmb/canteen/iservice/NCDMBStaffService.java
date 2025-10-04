package com.ncdmb.canteen.iservice;

import com.ncdmb.canteen.dtos.request.NCDMBStaffRequestDto;
import com.ncdmb.canteen.dtos.request.UserDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.NCDMBStaff;

import java.util.List;

public interface NCDMBStaffService extends CanteenService {

    OperationalResponse addStaff(NCDMBStaffRequestDto dto);
    OperationalResponse alterStaffStatus(int staffId, String status);
    List<NCDMBStaff> allStaff();
    OperationalResponse addStaffUser(UserDto dto);
}
