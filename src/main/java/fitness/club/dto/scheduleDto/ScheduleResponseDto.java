package fitness.club.dto.scheduleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor@NoArgsConstructor
public class ScheduleResponseDto {
    private  Integer id;
    private  Integer clientId;
    private  Integer coachId;
    private  Integer workoutId;
    private  LocalDate date;
}
