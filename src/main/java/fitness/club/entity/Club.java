package fitness.club.entity;

import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Builder
@Data
@Table(name = "club", schema = "fitness_club")
public class Club extends BaseEntity{
    @Column(name = "address")
    private  String address;
    @Column(name = "name")
    private  String name;
}
