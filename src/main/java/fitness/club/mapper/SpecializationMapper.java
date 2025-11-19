package fitness.club.mapper;

import fitness.club.dto.specializationDto.SpecializationRequestDto;
import fitness.club.dto.specializationDto.SpecializationResponseDto;
import fitness.club.entity.Specialization;

public class SpecializationMapper implements BaseMapper<SpecializationRequestDto,
        SpecializationResponseDto, Specialization> {

    @Override
    public Specialization toEntity(SpecializationRequestDto specializationRequestDto) {
        return Specialization.builder().name(specializationRequestDto.getName()).build();
    }

    @Override
    public SpecializationResponseDto toResponseDto(Specialization response) {
        return SpecializationResponseDto.builder().id(response.getId())
                .name(response.getName()).build();
    }
}
