package fitness.club.service;

import fitness.club.dto.clinetDto.ClientRequestDto;
import fitness.club.dto.clinetDto.ClientResponseDto;
import fitness.club.entity.Client;
import fitness.club.entity.Club;
import fitness.club.exeptions.ServiceException;
import fitness.club.mapper.ClientMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ClientService {
    ClientMapper clientMapper = new ClientMapper();

    public ClientResponseDto save(ClientRequestDto clientRequestDto) {
        return clientMapper.toResponseDto(Client.provider.add(clientMapper.toEntity(clientRequestDto)));
    }

    public ClientResponseDto update(ClientRequestDto requestDto, Integer id) {
        Client client = clientMapper.toEntity(requestDto);
        client.setId(id);
        return clientMapper.toResponseDto(Client.provider.update(client));
    }

    public List<ClientResponseDto> findAll() {
        return Client.provider.findAll().stream()
                .map(clientMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public ClientResponseDto findById(Integer id) {
        return Client.provider.findById(id)
                .map(clientMapper::toResponseDto)
                .orElse(null);
    }
    public ClientResponseDto findByEmail(String email) {
        return Client.provider.findByEmail(email)
                .map(clientMapper::toResponseDto)
                .orElse(null);
                //.orElseThrow(() -> new ServiceException("Client with email: " + email + " not found"));
    }

    public void delete(Integer id) {
        Client.provider.delete(id);
    }
}
