package fitness.club.entity;

import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
@Table(name = "Trainer_specialization", schema = "fitness_club")
public class CoachSpecialization {
    @Column(name = "coach_id")
    private Integer coachId;
    @Column(name = "spec_id")
    private Integer specId;
}
