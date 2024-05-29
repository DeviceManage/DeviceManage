package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Siteuser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteuserRepository extends JpaRepository<Siteuser, Long> {

    Siteuser findByUname(String uname);

}
