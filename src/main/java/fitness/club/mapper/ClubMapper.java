package fitness.club.mapper;

import fitness.club.dto.clubDto.ClubRequestDto;
import fitness.club.dto.clubDto.ClubResponseDto;
import fitness.club.entity.Club;

public class ClubMapper implements BaseMapper<ClubRequestDto,ClubResponseDto, Club> {

    @Override
    public Club toEntity(ClubRequestDto dto) {
        return Club.builder().address(dto.getAddress())
                .name(dto.getName()).build();
    }
    @Override
    public ClubResponseDto toResponseDto(Club cl) {
        return ClubResponseDto.builder().address(cl.getAddress())
                .id(cl.getId()).name(cl.getName()).build();
    }
}
