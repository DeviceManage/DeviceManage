package com.hit.devicemanage.controller;

import com.hit.devicemanage.entity.Devicegroup;
import com.hit.devicemanage.entity.Siteuser;
import com.hit.devicemanage.service.DeviceService;
import com.hit.devicemanage.service.DevicegroupService;
import com.hit.devicemanage.service.SiteuserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    private SiteuserService siteuserService;
    @Autowired
    private DevicegroupService devicegroupService;
    @GetMapping({"/",""})
    public String index(Model model, HttpServletRequest request, HttpSession session) {
        String username = session.getAttribute("username").toString();
        if (username == null) {
            return "redirect:/login";
        }
        Siteuser user = siteuserService.findByUname(username);
        int uprivi = user.getUprivi();
        int ugroup = user.getUgroup();
        if (uprivi == 5) { // 组管理员，跳转到特定组的管理页面
            return "redirect:/admin/group/" + ugroup;
        }
        if (uprivi == 7) { // 超级管理员，可以管理所有组
            List<Devicegroup> allGroups = devicegroupService.getAllDevicegroups();
            List<Siteuser> ungroupedUsers = siteuserService.findByGroup(-1);
            model.addAttribute("ungroupedUsers", ungroupedUsers);
            model.addAttribute("groups", allGroups);
            return "admin_root";
        }
        // 非管理员
        model.addAttribute("err","无权访问");
        model.addAttribute("ret","/main");
        return "error";
    }

    @PostMapping("/createGroup")
    public String createGroup(@RequestParam("groupname") String groupname, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null || currentUser.getUprivi() < 7) {
            model.addAttribute("err", "无权访问");
            model.addAttribute("ret", "/main");
            return "error";
        }

        Devicegroup group = new Devicegroup();
        group.setGname(groupname);
        group.setGcode(generateInvitationCode());
        devicegroupService.saveDevicegroup(group);

        return "redirect:/admin/";
    }

    @PostMapping("/deleteGroup")
    public String deleteGroup(@RequestParam("groupId") int groupId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null || currentUser.getUprivi() < 7) {
            model.addAttribute("err", "无权访问");
            model.addAttribute("ret", "/main");
            return "error";
        }
        List<Siteuser> userlist = siteuserService.findByGroup(groupId);
        for (Siteuser user : userlist) {
            user.setUgroup(-1);
        }

        devicegroupService.deleteDevicegroupById(groupId);

        return "redirect:/admin/";
    }

    @GetMapping("/group/{groupId}")
    public String manageGroup(@PathVariable("groupId") int groupId, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser user = siteuserService.findByUname(username);
        int uprivi = user.getUprivi();

        if (uprivi < 5 || user.getUgroup() != groupId && uprivi != 7) {
            model.addAttribute("err", "无权访问");
            model.addAttribute("ret", "/main");
            return "error";
        }

        Devicegroup group = devicegroupService.getDevicegroupById(groupId).orElse(null);
        List<Siteuser> groupMembers = siteuserService.findByGroup(groupId);

        model.addAttribute("group", group);
        model.addAttribute("groupMembers", groupMembers);

        return "admin_group";
    }
    @PostMapping("/group/{groupId}/updateUser")
    public String updateUser(@PathVariable("groupId") int groupId,
                             @RequestParam("userId") int userId,
                             @RequestParam("uprivi") int uprivi,
                             HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null || (currentUser.getUprivi() < 5 && currentUser.getUgroup() != groupId && currentUser.getUprivi()!= 7 )) {
            model.addAttribute("err", "无权访问");
            model.addAttribute("ret", "/main");
            return "error";
        }

        if (currentUser.getUprivi() < uprivi) {
            model.addAttribute("err", "禁止越权授予权限");
            model.addAttribute("ret", "/main");
            return "error";
        }

        Siteuser user = siteuserService.findByUid(userId);
        if (user != null) {
            user.setUgroup(groupId);
            user.setUprivi(uprivi);
            siteuserService.saveSiteuser(user);
        }

        return "redirect:/admin/group/" + groupId;
    }

    @PostMapping("/group/{groupId}/removeUser")
    public String removeUser(@PathVariable("groupId") int groupId,
                             @RequestParam("userId") int userId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null || (currentUser.getUprivi() < 5 && currentUser.getUgroup() != groupId && currentUser.getUprivi()!= 7)) {
            model.addAttribute("err", "无权访问");
            model.addAttribute("ret", "/main");
            return "error";
        }

        Siteuser user = siteuserService.findByUid(userId);
        if (user != null) {
            user.setUgroup(-1);
            siteuserService.saveSiteuser(user);
        }

        return "redirect:/admin/group/" + groupId;
    }

    @PostMapping("/group/{groupId}/updateGroupName")
    public String updateGroupName(@PathVariable("groupId") int groupId,
                                  @RequestParam("groupName") String groupName,
                                  HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null) {
            return "redirect:/";
        }

        Devicegroup group = devicegroupService.getDevicegroupById(groupId).orElse(null);
        if (group != null) {
            group.setGname(groupName);
            devicegroupService.saveDevicegroup(group);
        }

        return "redirect:/admin/group/" + groupId;
    }

    @PostMapping("/group/{groupId}/generateInvite")
    public String generateInvite(@PathVariable("groupId") int groupId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null) {
            return "redirect:/";
        }

        Devicegroup group = devicegroupService.getDevicegroupById(groupId).orElse(null);
        if (group != null) {
            group.setGcode(generateInvitationCode());
            devicegroupService.saveDevicegroup(group);
        }

        return "redirect:/admin/group/" + groupId;
    }

    private String generateInvitationCode() {
        // 生成随机邀请码的逻辑
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8); // 示例
    }

    @PostMapping("/modifyUser")
    public String modifyUser(@RequestParam("userId") int userId,
                             @RequestParam("groupId") int groupId,
                             @RequestParam("uprivi") int uprivi,
                             HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null || currentUser.getUprivi() < 7) {
            model.addAttribute("err", "无权访问");
            model.addAttribute("ret", "/main");
            return "error";
        }

        Siteuser user = siteuserService.findByUid(userId);
        if (user != null) {
            user.setUgroup(groupId);
            user.setUprivi(uprivi);
            siteuserService.saveSiteuser(user);
        }

        return "redirect:/admin/";
    }

    @PostMapping("/removeUser")
    public String deleteUser(@RequestParam("userId") int userId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Siteuser currentUser = siteuserService.findByUname(username);
        if (currentUser == null || currentUser.getUprivi() < 7) {
            model.addAttribute("err", "无权访问");
            model.addAttribute("ret", "/main");
            return "error";
        }

        siteuserService.deleteSiteuserByUid(userId);
        return "redirect:/admin/";
    }
}
