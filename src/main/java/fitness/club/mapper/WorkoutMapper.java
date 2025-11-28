package fitness.club.mapper;

import fitness.club.dto.workoutDto.WorkoutRequestDto;
import fitness.club.dto.workoutDto.WorkoutResponseDto;
import fitness.club.entity.Workout;

public class WorkoutMapper implements BaseMapper<WorkoutRequestDto, WorkoutResponseDto, Workout>{
    @Override
    public Workout toEntity(WorkoutRequestDto workoutRequestDto) {
        return Workout.builder().name(workoutRequestDto.getName())
                .duration(workoutRequestDto.getDuration()).build();
    }

    @Override
    public WorkoutResponseDto toResponseDto(Workout response) {
        return WorkoutResponseDto.builder().id(response.getId()).name(response.getName())
                .duration(response.getDuration()).build();
    }
}
