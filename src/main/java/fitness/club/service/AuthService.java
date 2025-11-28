package fitness.club.service;

import fitness.club.dto.clinetDto.ClientRequestDto;
import fitness.club.dto.clinetDto.ClientResponseDto;
import fitness.club.entity.Client;
import fitness.club.exeptions.AuthException;
import fitness.club.mapper.ClientMapper;
import fitness.club.repository.ClientRepository;

import java.util.Optional;


public class AuthService {
    ClientMapper clientMapper = new ClientMapper();

    public ClientResponseDto login(String email){
        return Client.provider.findByEmail(email)
                .map(clientMapper::toResponseDto)
                .orElseThrow(() -> new AuthException("User not found"));

    }

    public ClientResponseDto register(ClientRequestDto dto){
        String email = dto.getEmail();
        Client client = Client.provider.add(clientMapper.toEntity(dto));
        return clientMapper.toResponseDto(client);
    }


}
