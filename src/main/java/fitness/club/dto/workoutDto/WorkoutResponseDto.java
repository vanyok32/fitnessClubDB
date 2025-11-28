package fitness.club.dto.workoutDto;

import lombok.Builder;

@Builder
public class WorkoutResponseDto {
    private Integer id;
    private String name;
    private Integer duration;
}
