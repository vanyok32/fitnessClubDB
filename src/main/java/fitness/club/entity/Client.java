package fitness.club.entity;

import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@Table(name = "client", schema = "fitness_club")
public class Client extends BaseEntity{
    @Column(name = "club_id")
    private Integer clubId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
}
