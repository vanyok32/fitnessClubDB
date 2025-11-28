package fitness.club.service;

import fitness.club.entity.Membership;
import fitness.club.exeptions.ServiceException;

import java.util.List;

public class MembershipService {

    public Membership findByClientId(Integer id){
        return Membership.provider.findById(id).orElseThrow(() -> new ServiceException
                ("Membership with clientId " + id + " not found"));
    }

    public Membership activate(Integer id){
        Membership membership = findByClientId(id);
        return Membership.provider.activate(membership);
    }

    public List<Membership> findAll() {return Membership.provider.findAll();}

    public Membership save(Membership membership) {return Membership.provider.add(membership);}

    public Membership update(Membership membership) {return Membership.provider.update(membership);}

    public void delete(Integer id) {Membership.provider.delete(id);}
}
