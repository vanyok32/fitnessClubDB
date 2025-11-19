package fitness.club.mapper;

import fitness.club.dto.scheduleDto.ScheduleRequestDto;
import fitness.club.dto.scheduleDto.ScheduleResponseDto;
import fitness.club.entity.Schedule;

public class ScheduleMapper implements BaseMapper<ScheduleRequestDto, ScheduleResponseDto, Schedule> {

    @Override
    public Schedule toEntity(ScheduleRequestDto dto) {
        return Schedule.builder().clientId(dto.getClientId())
                .coachId(dto.getCoachId()).date(dto.getDate())
                .workoutId(dto.getWorkoutId()).build();
    }

    @Override
    public ScheduleResponseDto toResponseDto(Schedule sc) {
        return ScheduleResponseDto.builder().id(sc.getId()).clientId(sc.getClientId())
                .coachId(sc.getCoachId()).date(sc.getDate()).workoutId(sc.getWorkoutId()).build();
    }
}
