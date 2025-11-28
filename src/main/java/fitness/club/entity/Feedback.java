package fitness.club.entity;

import fitness.club.repository.FeedbackRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.Data;

@Data
@Table(name = "feedback", schema = "fitness_club")
public class Feedback {

    public static FeedbackRepository provider = new FeedbackRepository();

    @Column(name = "shedule_id")
    private int sheduleId;
    @Column(name = "rating")
    private int rating;
    @Column(name ="comment")
    private String comment;
}
