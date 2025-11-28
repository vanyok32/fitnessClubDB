package fitness.club.service;

import fitness.club.dto.scheduleDto.ScheduleRequestDto;
import fitness.club.dto.scheduleDto.ScheduleResponseDto;
import fitness.club.entity.Schedule;
import fitness.club.exeptions.ServiceException;
import fitness.club.mapper.ScheduleMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ScheduleService {
    private final ScheduleMapper scheduleMapper = new ScheduleMapper();

    public ScheduleResponseDto findById(Integer id) {
        return scheduleMapper.toResponseDto(Schedule.provider.findById(id)
                .orElseThrow(() -> new ServiceException("Schedule not found")));
    }

    public List<Schedule> findAll() {
        return Schedule.provider.findAll();
    }

    public ScheduleResponseDto save(ScheduleRequestDto schedule) {
        return scheduleMapper.toResponseDto(Schedule.provider.add(scheduleMapper.toEntity(schedule)));
    }

    public ScheduleResponseDto update(ScheduleRequestDto schedule) {
        return scheduleMapper.toResponseDto(Schedule.provider.update(scheduleMapper.toEntity(schedule)));
    }

    public void delete(Integer id) {Schedule.provider.delete(id);}

    public List<ScheduleResponseDto> findByCoachId(Integer id) {
        return Schedule.provider.findByCoachOrClientId(id, true).stream()
                .map(scheduleMapper::toResponseDto).collect(Collectors.toList());
    }
    public List<ScheduleResponseDto> findByClientId(Integer id) {
        return Schedule.provider.findByCoachOrClientId(id, false).stream()
                .map(scheduleMapper::toResponseDto).collect(Collectors.toList());
    }
}
