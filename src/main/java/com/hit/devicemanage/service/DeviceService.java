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

    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }

    public Device updateDevice(Long id, Device updatedevice) {
        return deviceRepository.findById(id).map(device -> {
            device.setDname(updatedevice.getDname());
            device.setDtype(updatedevice.getDtype());
            device.setDimage(updatedevice.getDimage());
            device.setBuydate(updatedevice.getBuydate()); // ensure date is updated
            device.setDetail(updatedevice.getDetail());  // include detail update
            device.setDgroup(updatedevice.getDgroup());
            device.setDprivi(updatedevice.getDprivi());
            device.setDstate(updatedevice.getDstate());

            return deviceRepository.save(device);
        }).orElseGet(() -> {
            updatedevice.setDid(id); // just to ensure correct ID in case of creation
            return deviceRepository.save(updatedevice);
        });
    }
    public void saveDevice(Device device) {
        deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }
}