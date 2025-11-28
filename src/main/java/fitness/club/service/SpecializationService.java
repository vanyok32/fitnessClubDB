package fitness.club.service;

import fitness.club.dto.specializationDto.SpecializationRequestDto;
import fitness.club.dto.specializationDto.SpecializationResponseDto;
import fitness.club.entity.Specialization;
import fitness.club.exeptions.ServiceException;
import fitness.club.mapper.SpecializationMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SpecializationService {
    private final SpecializationMapper mapper = new SpecializationMapper();

    public SpecializationResponseDto save(SpecializationRequestDto dto) {
        return mapper.toResponseDto(Specialization.provider.add(mapper.toEntity(dto)));
    }

    public void delete(Integer id) {Specialization.provider.delete(id);}

    public List<SpecializationResponseDto> findAll(){
        return Specialization.provider.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public SpecializationResponseDto findById(Integer id) {
        return mapper.toResponseDto(Specialization.provider.findById(id).orElseThrow(()
                -> new ServiceException("Specialization with id " + id + " not found")));
    }

    public SpecializationResponseDto update(SpecializationRequestDto dto, Integer id) {
        Specialization specialization = mapper.toEntity(dto);
        specialization.setId(id);
        return mapper.toResponseDto(Specialization.provider.update(specialization));
    }

}
