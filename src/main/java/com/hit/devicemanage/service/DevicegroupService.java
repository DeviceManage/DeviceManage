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

    public Optional<Devicegroup> getDevicegroupById(Integer id) {
        return devicegroupRepository.findById(id);
    }

    public Devicegroup findDevicegroupByGcode(String gcode) {
        return devicegroupRepository.findByGcode(gcode);
    }

    public void saveDevicegroup(Devicegroup group) {
        devicegroupRepository.save(group);
    }

    public void deleteDevicegroupById(int groupId) {
        devicegroupRepository.deleteById(groupId);
    }
}
