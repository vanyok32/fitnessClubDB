package fitness.club.mapper;

import fitness.club.dto.coachDto.CoachRequestDto;
import fitness.club.dto.coachDto.CoachResponseDto;
import fitness.club.entity.Coach;

public class CoachMapper implements BaseMapper<CoachRequestDto, CoachResponseDto, Coach> {

    @Override
    public Coach toEntity(CoachRequestDto dto) {
        return Coach.builder().name(dto.getName()).email(dto.getEmail())
                .clubId(dto.getClubId()).build();
    }

    @Override
    public CoachResponseDto toResponseDto(Coach c) {
        return CoachResponseDto.builder().id(c.getId())
                .clubId(c.getClubId()).email(c.getEmail())
                .name(c.getName()).build();
    }
}
