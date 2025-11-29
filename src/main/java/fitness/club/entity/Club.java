package fitness.club.entity;

import fitness.club.repository.ClubRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "club", schema = "fitness_club")
public class Club extends BaseEntity{

    public static final ClubRepository provider = new ClubRepository();

    @Column(name = "address")
    private  String address;
    @Column(name = "name")
    private  String name;
}
