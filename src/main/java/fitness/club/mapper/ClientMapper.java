package fitness.club.mapper;

import fitness.club.dto.clinetDto.ClientResponseDto;
import fitness.club.dto.clinetDto.ClientRequestDto;
import fitness.club.entity.Client;

public class ClientMapper implements BaseMapper<ClientRequestDto, ClientResponseDto, Client> {

    @Override
    public Client toEntity(ClientRequestDto dto) {
        return Client.builder().email(dto.getEmail())
                .name(dto.getName()).clubId(dto.getClubId()).build();
    }

    @Override
    public ClientResponseDto toResponseDto(Client cl) {
        return ClientResponseDto.builder().id(cl.getId())
                .clubId(cl.getClubId()).email(cl.getEmail())
                .name(cl.getName()).build();
    }
}
