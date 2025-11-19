package fitness.club.dto.scheduleDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class ScheduleRequestDto {
    private final Integer clientId;
    private final Integer coachId;
    private final Integer workoutId;
    private final LocalDate date;
}
