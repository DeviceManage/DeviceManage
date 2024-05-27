package com.hit.devicemanage.controller;

import com.hit.devicemanage.entity.Device;
import com.hit.devicemanage.service.DeviceService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private DeviceService deviceService;
    @GetMapping("/")
    public String index() {
        return "redirect:/devices/";
    }
}

/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    // 查询所有设备
    @GetMapping
    public String getAllDevices(Model model) {
        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        return "devices";  // 返回Thymeleaf模板
    }

    // 查询单个设备
    @GetMapping("/{id}")
    public String getDeviceById(@PathVariable Long id, Model model) {
        Device device = deviceService.getDeviceById(id).orElse(null);
        model.addAttribute("device", device);
        return "device-detail";  // 返回Thymeleaf模板
    }

    // 修改单个设备
    @PostMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device updatedDevice) {
        Device device = deviceService.updateDevice(id, updatedDevice);
        return ResponseEntity.ok(device);
    }
}
 */