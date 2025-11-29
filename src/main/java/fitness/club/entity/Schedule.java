package fitness.club.entity;

import fitness.club.repository.ScheduleRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.*;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule", schema = "fitness_club")
public class Schedule extends BaseEntity{

    public static ScheduleRepository provider = new ScheduleRepository();

    @Column(name = "client_id")
    private Integer clientId;
    @Column(name = "coach_id")
    private Integer coachId;
    @Column(name = "workout_id")
    private Integer workoutId;
    @Column(name = "date")
    private LocalDate date;
}
