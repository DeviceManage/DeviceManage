package com.hit.devicemanage.controller;

import com.hit.devicemanage.entity.Device;
import com.hit.devicemanage.service.DeviceService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Formatter;
import java.util.List;

@Controller
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    DeviceService deviceService;
    @GetMapping("/")
    public String index(Model model) {
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Device device = deviceService.getDeviceById(id).orElseThrow(() -> new IllegalArgumentException("Invalid device Id: " + id));
        model.addAttribute("device", device);
        return "edit-device";  // 返回Thymeleaf模板 'edit-device.html'
    }

    @PostMapping("/edit/{id}")
    public String updateDevice(@PathVariable("id") Long id, @ModelAttribute("device") Device updatedDevice,
                               @RequestParam("image") MultipartFile imageFile) throws Exception{

            if (!imageFile.isEmpty()) {
                // 生成文件名
                String hash = hashTimestampAndFileName(Instant.now().toString() + imageFile.getOriginalFilename());
                String fileName = hash + ".jpg";
                Path filePath = Paths.get("src/main/resources/static/images/" + fileName);

                // 保存文件到指定目录
                Files.write(filePath, imageFile.getBytes());

                // 更新设备的dimage字段
                updatedDevice.setDimage(hash);
            }
            deviceService.updateDevice(id, updatedDevice);


        return "redirect:/devices/";  // 重定向到设备列表页面
    }

    private String hashTimestampAndFileName(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        return byteArray2Hex(hash).substring(0, 32); // 截取前32位
    }

    private String byteArray2Hex(final byte[] hash) {
        try (Formatter formatter = new Formatter()) {
            for (final byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }
}