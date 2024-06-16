package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Siteuser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteuserRepository extends JpaRepository<Siteuser, Integer> {

    Siteuser findByUname(String uname);

}
