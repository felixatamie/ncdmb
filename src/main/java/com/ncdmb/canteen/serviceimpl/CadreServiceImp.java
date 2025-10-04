package com.ncdmb.canteen.serviceimpl;

import com.ncdmb.canteen.dtos.request.CadreRequestDto;
import com.ncdmb.canteen.dtos.response.OperationalResponse;
import com.ncdmb.canteen.entity.Cadre;
import com.ncdmb.canteen.iservice.CadreService;
import com.ncdmb.canteen.repository.CadreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor

public class CadreServiceImp implements CadreService {

    private final CadreRepository cadreRepository;
    @Override
    public OperationalResponse addCadre(CadreRequestDto dto) {
        Cadre cadre = new Cadre();
        cadre.setName(dto.getName());
        cadre.setDescription(dto.getDescription());
        cadre.setNcdmbPercent(dto.getNcdmbPercent());
        cadre.setStaffPercent(dto.getStaffPercent());
        cadre.setCreatedAt(LocalDateTime.now());
        cadre.setModifiedAt(LocalDateTime.now());
        cadreRepository.save(cadre);
        return OperationalResponse.builder().message("Cadre successfully created").success(true).build();
    }

    @Override
    public List<Cadre> getAllCadres() {
        return (List<Cadre>) cadreRepository.findAll();
    }

    @Override
    public OperationalResponse editCadre(int id, CadreRequestDto dto) {
        Optional<Cadre> cadreOptional = cadreRepository.findById(id);

        if(cadreOptional.isEmpty())
            return OperationalResponse.builder().message("Cadre id: "+ id + " not found, update unsuccessful").success(false).build();

        cadreRepository.updateCadre(dto.getName(), dto.getNcdmbPercent(),dto.getStaffPercent(),LocalDateTime.now(),id);
        return OperationalResponse.builder().message("Cadre id: "+ id + " updated successfully").success(true).build();
    }
}
