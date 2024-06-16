package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Devicegroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevicegroupRepository  extends JpaRepository<Devicegroup,Integer>{
    public Devicegroup findByGcode(String gcode);
}
