package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    public List<Device> getDeviceByGroup(Integer group) {
        return deviceRepository.findByDgroup(group);
    }
    public List<Device> getBorrowedDevicesByGroup(int groupId) {
        return deviceRepository.findByTmpgid(groupId);
    }

    public Optional<Device> getDeviceById(Integer id) {
        return deviceRepository.findById(id);
    }

    public Device updateDevice(Integer id, Device updatedevice) {
        return deviceRepository.findById(id).map(device -> {
            device.setDname(updatedevice.getDname());
            device.setDtype(updatedevice.getDtype());
            device.setDimage(updatedevice.getDimage());
            device.setBuydate(updatedevice.getBuydate()); // ensure date is updated
            device.setDetail(updatedevice.getDetail());  // include detail update
            device.setDgroup(updatedevice.getDgroup());
            device.setDprivi(updatedevice.getDprivi());
            device.setDstate(updatedevice.getDstate());

            device.setTmpgid(updatedevice.getTmpgid()); // maybe a BUG

            return deviceRepository.save(device);
        }).orElseGet(() -> {
            updatedevice.setDid(id); // just to ensure correct ID in case of creation
            return deviceRepository.save(updatedevice);
        });
    }
    public void saveDevice(Device device) {
        deviceRepository.save(device);
    }

    public void deleteDevice(Integer id) {
        deviceRepository.deleteById(id);
    }
}