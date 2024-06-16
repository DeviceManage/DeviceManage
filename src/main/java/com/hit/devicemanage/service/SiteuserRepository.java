package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Siteuser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteuserRepository extends JpaRepository<Siteuser, Integer> {

    Siteuser findByUname(String uname);

    List<Siteuser> findByUgroup(Integer ugroup);
}
