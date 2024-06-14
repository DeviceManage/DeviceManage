package com.hit.devicemanage.controller;

import com.hit.devicemanage.entity.Device;
import com.hit.devicemanage.entity.Devicegroup;
import com.hit.devicemanage.service.DeviceService;
import com.hit.devicemanage.service.SiteuserRepository;
import com.hit.devicemanage.service.SiteuserService;
import com.hit.devicemanage.service.DevicegroupService;
import com.hit.devicemanage.entity.Siteuser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

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
                Optional<Devicegroup> dgroup = devicegroupService.getDevicegroupById((long) ugroup);
                System.out.println(dgroup);
                if(dgroup.isPresent()) {

                    model.addAttribute("devicegroup",dgroup.get());
                }
                else{
                    model.addAttribute("devicegroup",null);
                }
            }

            model.addAttribute("username", username);
        }
        List<Device> devices = deviceService.getAllDevices();
        model.addAttribute("devices", devices);
        return "main.html";
    }
}
