package com.hit.devicemanage.controller;

import com.hit.devicemanage.entity.Device;
import com.hit.devicemanage.entity.Devicegroup;
import com.hit.devicemanage.service.DeviceService;
import com.hit.devicemanage.service.SiteuserService;
import com.hit.devicemanage.service.DevicegroupService;
import com.hit.devicemanage.entity.Siteuser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private SiteuserService siteuserService;
    @Autowired
    private DevicegroupService devicegroupService;

    @GetMapping({"/main","/main/"})
    public String main(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }
        else{
            Siteuser siteuser = siteuserService.findByUname(username);
            if (siteuser == null) {
                return "redirect:/";
            }
            int ugroup = siteuser.getUgroup();
            model.addAttribute("ugroup",ugroup);
            if (ugroup != -1) {
                Optional<Devicegroup> dgroup = devicegroupService.getDevicegroupById((Integer) ugroup);
//                System.out.println(dgroup);
                if(dgroup.isPresent()) {

                    model.addAttribute("devicegroup",dgroup.get());
                }
                else{
                    model.addAttribute("devicegroup",null);
                }
            }

            model.addAttribute("username", username);
        }
        Siteuser siteuser = siteuserService.findByUname(username);
        int uprivi = siteuser.getUprivi();
        int ugroup = siteuser.getUgroup();
        List<Device> devices = deviceService.getAllDevices();
        if (uprivi == 7) model.addAttribute("devices", devices);
        else model.addAttribute("devices", deviceService.getDeviceByGroup(ugroup));
        return "main.html";
    }

    @GetMapping("/main/join")
    public String joinGroupStatic(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }

        String iserr = request.getParameter("iserr");
        if (iserr == null) {
            model.addAttribute("iserr", "0");
            return "joingroup.html";
        }
        model.addAttribute("iserr", "1");
        return "joingroup.html";
    }

    @PostMapping("/main/join")
    public String joinGroup(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }

        String dgcode = request.getParameter("dgcode");
        if (dgcode == null) {
            return "redirect:/";
        }
        Devicegroup dgroup = devicegroupService.findDevicegroupByGcode(dgcode);
        if (dgroup == null) {
            return "redirect:/main/join?iserr=1";
        }
        Siteuser siteuser = siteuserService.findByUname(username);
        if (siteuser == null) {
            return "redirect:/";
        }
        int ugroup = Math.toIntExact(dgroup.getGid());
        int uprivi = 3;
        siteuser.setUgroup(ugroup);
        siteuser.setUprivi(uprivi);
        siteuserService.saveSiteuser(siteuser);
        return "redirect:/main";
    }

    @GetMapping("/main/reset_group")
    public String resetGroup(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }
        Siteuser siteuser = siteuserService.findByUname(username);
        if (siteuser == null) {
            return "redirect:/";
        }
        siteuser.setUgroup(-1);
        siteuser.setUprivi(0);
        siteuserService.saveSiteuser(siteuser);
        return "redirect:/main";
    }

}
