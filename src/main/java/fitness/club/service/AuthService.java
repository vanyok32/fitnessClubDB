package fitness.club.service;

import fitness.club.dto.clinetDto.ClientRequestDto;
import fitness.club.dto.clinetDto.ClientResponseDto;
import fitness.club.entity.Client;
import fitness.club.entity.Membership;
import fitness.club.exeptions.AuthException;
import fitness.club.mapper.ClientMapper;
import fitness.club.repository.ClientRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;


public class AuthService {
    ClientMapper clientMapper = new ClientMapper();
    MembershipService membershipService = new MembershipService();

    public ClientResponseDto login(String email, String password){
        Client client = Client.provider.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));

        if (client.getPassword() == null || !client.getPassword().equals(password)) {
            throw new AuthException("Invalid password");
        }

        return clientMapper.toResponseDto(client);
    }

    public ClientResponseDto register(ClientRequestDto dto){
        // Проверяем, не существует ли уже пользователь с таким email
        if (Client.provider.findByEmail(dto.getEmail()).isPresent()) {
            throw new AuthException("User with this email already exists");
        }

        Client client = Client.provider.add(clientMapper.toEntity(dto));

        // Создаем и активируем membership для нового клиента
        Membership membership = Membership.builder()
                .clientId(client.getId())
                .startDate(Date.valueOf(LocalDate.now()))
                .endDate(Date.valueOf(LocalDate.now().plusMonths(1)))
                .isActive(true)
                .build();
        membershipService.save(membership);

        return clientMapper.toResponseDto(client);
    }


}
