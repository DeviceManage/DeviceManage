package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Devicegroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevicegroupService {
    private final DevicegroupRepository devicegroupRepository;

    @Autowired
    public DevicegroupService(DevicegroupRepository devicegroupRepository) {
        this.devicegroupRepository = devicegroupRepository;
    }

    public List<Devicegroup> getAllDevicegroups() {
        return devicegroupRepository.findAll();
    }

    public Optional<Devicegroup> getDevicegroupById(Long id) {
        return devicegroupRepository.findById(id);
    }


}
