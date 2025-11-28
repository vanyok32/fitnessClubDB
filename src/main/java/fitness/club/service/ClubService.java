package fitness.club.service;

import fitness.club.dto.clubDto.ClubRequestDto;
import fitness.club.dto.clubDto.ClubResponseDto;
import fitness.club.entity.Club;
import fitness.club.exeptions.ServiceException;
import fitness.club.mapper.ClubMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClubService {
    private final ClubMapper clubMapper = new ClubMapper();

    public List<ClubResponseDto> getAllClubs() {
        return Club.provider.findAll()
                .stream()
                .map(clubMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteClub(Integer id) {Club.provider.delete(id);}

    public ClubResponseDto update(ClubRequestDto requestDto, Integer id) {
        Club club = clubMapper.toEntity(requestDto);
        club.setId(id);
        return clubMapper.toResponseDto(Club.provider.update(club));
    }

    public ClubResponseDto getClubById(Integer clubId) {
        return Club.provider.findById(clubId)
                .map(clubMapper::toResponseDto)
                .orElseThrow(() -> new ServiceException("Club with id " + clubId + " not found"));
    }
}
