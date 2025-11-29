package fitness.club.entity;

import fitness.club.repository.SpecializationRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "specialization", schema = "fitness_club")
public class Specialization extends BaseEntity{

    public static SpecializationRepository provider = new SpecializationRepository();

    @Column(name = "name")
    private String name;
}
