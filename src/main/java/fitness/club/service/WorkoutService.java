package fitness.club.service;

import fitness.club.dto.workoutDto.WorkoutRequestDto;
import fitness.club.dto.workoutDto.WorkoutResponseDto;
import fitness.club.entity.Workout;
import fitness.club.exeptions.ServiceException;
import fitness.club.mapper.WorkoutMapper;
import fitness.club.repository.WorkoutRepository;

import java.util.List;
import java.util.stream.Collectors;

public class WorkoutService {

    private final WorkoutMapper mapper = new WorkoutMapper();

    public WorkoutResponseDto findById(int id) {
        return Workout.provider.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new ServiceException("Workout with id: " + id + " not found"));
    }

    public List<WorkoutResponseDto> findAll() {
        return Workout.provider.findAll().stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public void delete(int id) {
        Workout.provider.delete(id);
    }

    public WorkoutResponseDto save(WorkoutRequestDto workout) {
        return mapper.toResponseDto(Workout.provider.add(mapper.toEntity(workout)));
    }

    public WorkoutResponseDto update(WorkoutRequestDto workout, Integer id) {
        Workout w = mapper.toEntity(workout);
        w.setId(w.getId());
        return mapper.toResponseDto(Workout.provider.update(w));
    }
}
