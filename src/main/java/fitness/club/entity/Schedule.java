package fitness.club.entity;

import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@Table(name = "schedule", schema = "fitness_club")
public class Schedule extends BaseEntity{
    @Column(name = "client_id")
    private Integer clientId;
    @Column(name = "coach_id")
    private Integer coachId;
    @Column(name = "workout_id")
    private Integer workoutId;
    @Column(name = "date")
    private LocalDate date;
}
