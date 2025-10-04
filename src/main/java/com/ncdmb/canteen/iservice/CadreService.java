package com.ncdmb.canteen.iservice;

import com.ncdmb.canteen.dtos.request.CadreRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Cadre;

import java.util.List;

public interface CadreService {
    OperationalResponse addCadre(CadreRequestDto dto);
    List<Cadre> getAllCadres();
    OperationalResponse editCadre(int id, CadreRequestDto dto);

}
