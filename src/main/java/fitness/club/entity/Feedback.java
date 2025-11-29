package fitness.club.entity;

import fitness.club.repository.FeedbackRepository;
import fitness.club.util.Column;
import fitness.club.util.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "feedback", schema = "fitness_club")
public class Feedback {

    public static FeedbackRepository provider = new FeedbackRepository();

    @Column(name = "schedule_id")
    private int scheduleId;
    @Column(name = "rating")
    private int rating;
    @Column(name ="comment")
    private String comment;
}
