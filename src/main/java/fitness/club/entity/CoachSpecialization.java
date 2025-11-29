package fitness.club.entity;

import fitness.club.repository.CoachSpecializationRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Trainer_specialization", schema = "fitness_club")
public class CoachSpecialization {

    public static CoachSpecializationRepository provider = new CoachSpecializationRepository();

    @Column(name = "coach_id")
    private Integer coachId;
    @Column(name = "spec_id")
    private Integer specId;
}
