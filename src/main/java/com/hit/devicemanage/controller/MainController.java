package com.hit.devicemanage.controller;

import com.hit.devicemanage.entity.Device;
import com.hit.devicemanage.service.DeviceService;
import com.hit.devicemanage.service.SiteuserService;
import com.hit.devicemanage.entity.Siteuser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SiteuserService siteuserService;

    @GetMapping("/main")
    public String main(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }
        else{
            model.addAttribute("username", username);
        }
        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        return "main.html";
    }
}
