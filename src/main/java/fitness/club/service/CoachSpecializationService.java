package fitness.club.service;

import fitness.club.entity.CoachSpecialization;
import fitness.club.exeptions.ServiceException;

import java.util.List;
import java.util.Optional;

public class CoachSpecializationService {

    public List<CoachSpecialization> findAll(){
        return CoachSpecialization.provider.findAll();
    }

    public CoachSpecialization findById(CoachSpecialization id){
        return Optional.of(CoachSpecialization.provider.findById(id)).get().orElseThrow(() ->
                new ServiceException("CoachSpec with coachId" + id.getCoachId() + "and SpecId" + id.getSpecId() + " not found"));
    }

    public CoachSpecialization save(CoachSpecialization coachSpecialization){
        return CoachSpecialization.provider.add(coachSpecialization);
    }
    public CoachSpecialization update(CoachSpecialization coachSpecialization){
        return CoachSpecialization.provider.update(coachSpecialization);
    }
    public void delete(CoachSpecialization id){
        CoachSpecialization.provider.delete(id);
    }


}
