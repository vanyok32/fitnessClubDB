package fitness.club.entity;

import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Data;

import java.util.Date;
@Data
@Table(name = "membership", schema = "fitness_club")
public class Membership {
    @Column(name = "client_id")
    private int clientId;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "is_active")
    private boolean isActive;
}
