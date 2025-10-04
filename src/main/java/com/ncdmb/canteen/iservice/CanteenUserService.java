package com.ncdmb.canteen.iservice;

import com.ncdmb.canteen.dtos.request.CanteenUserRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.CanteenUser;

import java.util.List;

public interface CanteenUserService {
    OperationalResponse addUser(CanteenUserRequestDto dto);
    OperationalResponse removeUser(int canteenId, int userId);
    List<CanteenUser> allCanteenUsers(int canteenId);
    OperationalResponse inactivateCanteenUser(int canteenId, int userId);

}
