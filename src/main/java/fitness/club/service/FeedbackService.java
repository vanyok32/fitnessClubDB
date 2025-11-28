package fitness.club.service;

import fitness.club.entity.Feedback;
import fitness.club.exeptions.ServiceException;

public class FeedbackService {

    public void delete(Integer scheduleId){
        Feedback.provider.delete(scheduleId);
    }
    public Feedback save(Feedback feedback){
        return Feedback.provider.add(feedback);
    }
    public Feedback findByScheduleId(Integer scheduleId){
        return Feedback.provider.findById(scheduleId).orElseThrow(() ->
                new ServiceException("Feedback with ScheduleId"+ scheduleId +  "not found"));
    }
    public Feedback update(Feedback feedback){
        return Feedback.provider.update(feedback);
    }
}
