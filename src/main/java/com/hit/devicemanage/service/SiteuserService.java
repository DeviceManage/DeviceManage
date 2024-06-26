package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Device;
import com.hit.devicemanage.entity.Siteuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteuserService {
    private final SiteuserRepository siteuserRepository;

    @Autowired
    public SiteuserService(SiteuserRepository siteuserRepository) {
        this.siteuserRepository = siteuserRepository;
    }

    public List<Siteuser> getAllUser() { return siteuserRepository.findAll(); }

    public Siteuser findByUname(String uname) { return siteuserRepository.findByUname(uname); }
    public List<Siteuser> findByGroup(Integer ugroup) { return siteuserRepository.findByUgroup(ugroup); }
    public Siteuser findByUid(Integer uid) { return siteuserRepository.findById(uid).get(); }
    public Siteuser updateUser(Integer id, Siteuser updateuser ) {
        return siteuserRepository.findById(id).map(siteuser ->{
            siteuser.setUname(updateuser.getUname());
            siteuser.setUpasswd(updateuser.getUpasswd());
            siteuser.setUgroup(updateuser.getUgroup());
            siteuser.setUprivi(updateuser.getUprivi());
            return siteuserRepository.save(siteuser);
        }).orElseGet(()->{
            updateuser.setUid(id);
            return siteuserRepository.save(updateuser);
        });
    }

    public void saveSiteuser(Siteuser siteuser) {
        siteuserRepository.save(siteuser);
    }
    public void deleteSiteuserByUid(Integer uid) { siteuserRepository.deleteById(uid); }

    public void newUser(String uname, String upasswd, Integer ugroup, Integer uprivi) {
        Siteuser siteuser = new Siteuser();
        siteuser.setUname(uname);
        siteuser.setUpasswd(upasswd);
        siteuser.setUprivi(uprivi);
        siteuser.setUgroup(ugroup);
        siteuserRepository.save(siteuser);
    }
}
