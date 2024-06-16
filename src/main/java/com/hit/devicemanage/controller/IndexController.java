package com.hit.devicemanage.controller;

import com.hit.devicemanage.service.DeviceService;
import com.hit.devicemanage.service.DevicegroupService;
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
    @Autowired
    private DevicegroupService devicegroupService;

    @GetMapping("/")
    public String index(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            model.addAttribute("isuser", "false");
        }
        else{
            model.addAttribute("isuser", "true");
            model.addAttribute("username", username);
        }
        Siteuser user = siteuserService.findByUname(username);
        if (user != null && user.getUprivi() >= 5) {
            model.addAttribute("ispriv", "true");
            model.addAttribute("groupname", user.getUgroup()==-1?"None":devicegroupService.getDevicegroupById(user.getUgroup()).get().getGname());
        }
        else model.addAttribute("ispriv", "false");
        return "index";
    }


    @GetMapping("/register")
    public String register(Model model, HttpSession session, HttpServletRequest request) {
        String isdone = request.getParameter("iserr");
        if (isdone == null) {
            model.addAttribute("iserr", "0");
            return "register";
        }
        if (isdone.equals("1")) {
            model.addAttribute("iserr", "1");
            return "register";
        }
        model.addAttribute("iserr", "0");
        return "register";
    }

    @PostMapping("/register/check")
    public String registerCheck(Model model, HttpSession session, HttpServletRequest request) {
        String user = request.getParameter("user");
        String pwd = request.getParameter("pwd");
        String pwd2 = request.getParameter("pwd2");
        String remember = request.getParameter("remember");
        System.out.println(user);
        Siteuser siteuser1 = siteuserService.findByUname(user);
        System.out.println(session.getAttribute("username"));
        if (siteuser1 != null) {
            return "redirect:/register?iserr=1";
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
            return "login";
        }
        if (iserr.equals("1")) {
            model.addAttribute("iserr", "1");
            return "login";
        }
        model.addAttribute("iserr", "0");
        return "login";
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

    @GetMapping("/resetpassword")
    public String resetPassword(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        Siteuser siteuser = siteuserService.findByUname(username);
        if (siteuser == null) {
            return "redirect:/login";
        }
        return "resetpassword";
    }

    @PostMapping("/resetpassword")
    public String resetPasswordCheck(Model model, HttpSession session, HttpServletRequest request) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        Siteuser siteuser = siteuserService.findByUname(username);
        if (siteuser == null) {
            return "redirect:/login";
        }
        String oldpwd = request.getParameter("oldpwd");
        String newpwd = request.getParameter("newpwd");
        if (!siteuser.getUpasswd().equals(oldpwd)) {
            return "redirect:/resetpassword?iserr=1";
        }
        siteuser.setUpasswd(newpwd);
        siteuserService.saveSiteuser(siteuser);
        return resetSessions(session, request);
    }

    @GetMapping("/index")
    public String index2(Model model, HttpSession session, HttpServletRequest request) {
        return "redirect:/";
    }
    @PostMapping("/index")
    public String index3(Model model, HttpSession session, HttpServletRequest request) {
        return "redirect:/";
    }

}
