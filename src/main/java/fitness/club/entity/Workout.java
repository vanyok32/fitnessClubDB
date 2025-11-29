package fitness.club.entity;

import fitness.club.repository.WorkoutRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout", schema = "fitness_club")
public class Workout extends BaseEntity{

    public static final WorkoutRepository provider = new WorkoutRepository();

    @Column(name = "name")
    private String name;
    @Column(name = "duration")
    private Integer duration;
}
