package fitness.club.dto.workoutDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutResponseDto {
    private Integer id;
    private String name;
    private Integer duration;
}
