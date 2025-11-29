package fitness.club;

import fitness.club.entity.Feedback;
import fitness.club.service.CoachService;
import fitness.club.service.FeedbackService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CoachService service = new CoachService();
        FeedbackService feedbackService = new FeedbackService();
        System.out.println(service.findById(1).toString());
        Feedback feedback = Feedback.builder().comment("все супер очень топ")
                .rating(5).scheduleId(1).build();
        feedbackService.save(feedback);
        System.out.println(feedbackService.findByScheduleId(1).toString());
    }
}