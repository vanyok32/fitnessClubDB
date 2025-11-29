package fitness.club.entity;

import fitness.club.repository.ClubRepository;
import fitness.club.repository.CoachRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coach", schema = "fitness_club")
public class Coach extends BaseEntity{

    public static final CoachRepository provider = new CoachRepository();

    @Column(name = "club_id")
    private  Integer clubId;
    @Column(name = "name")
    private  String name;
    @Column(name = "email")
    private  String email;
}
