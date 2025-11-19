package fitness.club.entity;

import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@Table(name = "workout", schema = "fitness_club")
public class Workout extends BaseEntity{
    @Column(name = "name")
    private String name;
    @Column(name = "duration")
    private Integer duration;
}
