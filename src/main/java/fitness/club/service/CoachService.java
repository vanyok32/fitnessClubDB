package fitness.club.service;

import fitness.club.dto.coachDto.CoachResponseDto;
import fitness.club.entity.Coach;
import fitness.club.exeptions.ServiceException;
import fitness.club.mapper.CoachMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CoachService {
    private final CoachMapper coachMapper = new CoachMapper();

    public CoachResponseDto findById(Integer id) {
        return Coach.provider.findById(id)
                .map(coachMapper::toResponseDto)
                .orElseThrow(() -> new ServiceException("Club with id " + id + " not found"));
    }

    public List<CoachResponseDto> findAll() {
        return Coach.provider.findAll()
                .stream()
                .map(coachMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CoachResponseDto> findByClub(Integer clubId) {
        return Coach.provider.findCoachesByClubId(clubId)
                .stream()
                .map(coachMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CoachResponseDto> findCoachesBySpecIdClubId(Integer clubId, Integer specId) {
        return Coach.provider.findCoachesBySpecIdClubId(clubId, specId)
                .stream()
                .map(coachMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {Coach.provider.delete(id);}
}
