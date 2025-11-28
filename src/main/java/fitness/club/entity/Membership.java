package fitness.club.entity;

import fitness.club.repository.MembershipRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Table(name = "membership", schema = "fitness_club")
@Builder
public class Membership {

    public static MembershipRepository provider = new MembershipRepository();

    @Column(name = "client_id")
    private int clientId;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "is_active")
    private boolean isActive;
}
