package com.hit.devicemanage.controller;

import com.hit.devicemanage.service.DeviceService;
import com.hit.devicemanage.service.SiteuserService;
import com.hit.devicemanage.entity.Siteuser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class IndexController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SiteuserService siteuserService;

    @GetMapping("/")
    public String index(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            model.addAttribute("isuser", "false");
        }
        else{
            model.addAttribute("isuser", "true");
        }
        return "index.html";
    }

    @GetMapping("/signin")
    public String signin(Model model, HttpSession session, HttpServletRequest request) {
        String isdone = request.getParameter("iserr");
        if (isdone == null) {
            model.addAttribute("iserr", "0");
            return "signin.html";
        }
        if (isdone.equals("1")) {
            model.addAttribute("iserr", "1");
            return "signin.html";
        }
        model.addAttribute("iserr", "0");
        return "signin.html";
    }

    @PostMapping("/signin/check")
    public String signinCheck(Model model, HttpSession session,HttpServletRequest request) {
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");
        String pwd2 = request.getParameter("pwd2");
        String remember = request.getParameter("remember");
        System.out.println(user);
        Siteuser siteuser1 = siteuserService.findByUname(user);
        System.out.println(session.getAttribute("username"));
        if (siteuser1 != null) {
            return "redirect:/signin?iserr=1";
        }
        siteuserService.newUser(user,pwd,-1,0);
        if (remember != null) {
            session.setAttribute("username", user);
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session, HttpServletRequest request) {
        String iserr = request.getParameter("iserr");
        if (iserr == null) {
            model.addAttribute("iserr", "0");
            return "login.html";
        }
        if (iserr.equals("1")) {
            model.addAttribute("iserr", "1");
            return "login.html";
        }
        model.addAttribute("iserr", "0");
        return "login.html";
    }

    @PostMapping("/login/check")
    public String loginCheck(HttpSession session,HttpServletRequest request) {
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");
        Siteuser siteuser = siteuserService.findByUname(user);
        if (siteuser == null) {
            return "redirect:/login?iserr=1";
        }
        System.out.println(siteuser.getUpasswd());
        if (!siteuser.getUpasswd().equals(pwd)) {
            return "redirect:/login?iserr=1";
        }
        session.setAttribute("username", user);
        return "redirect:/";
    }

    @GetMapping("/reset_sessions")
    public String resetSessions(HttpSession session, HttpServletRequest request) {
        session.removeAttribute("username");
        return "redirect:/";
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