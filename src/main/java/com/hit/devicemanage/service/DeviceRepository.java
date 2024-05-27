package com.hit.devicemanage.service;

import com.hit.devicemanage.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}