package com.hit.devicemanage.controller;

import com.hit.devicemanage.entity.Device;
import com.hit.devicemanage.entity.Siteuser;
import com.hit.devicemanage.service.DeviceService;

import com.hit.devicemanage.service.DevicegroupService;
import com.hit.devicemanage.service.SiteuserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    private SiteuserService siteuserService;
    @Autowired
    private DevicegroupService devicegroupService;

//    @GetMapping("/devices")
//    public String index(Model model) {
//        List<Device> devices = deviceService.getAllDevices();
//        model.addAttribute("devices", devices);
//        return "devices";  // 返回Thymeleaf模板
//    }
    // 查询单个设备
    @GetMapping("/{id}")
    public String getDeviceById(@PathVariable Integer id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }
        Device device = deviceService.getDeviceById(id).orElse(null);
        Siteuser user = siteuserService.findByUname(username);
        if (user.getUgroup() != device.getDgroup() && user.getUgroup() != device.getTmpgid() && device.getDgroup() != -1 && user.getUprivi() != 7) {
            model.addAttribute("err","无权访问");
            model.addAttribute("ret","/main");
            return "error";
        }
        model.addAttribute("device", device);
        if (device.getDgroup() != -1)
        model.addAttribute("group", devicegroupService.getDevicegroupById(device.getDgroup()).get().getGname());
        else model.addAttribute("group","");
        if (device.getDstate() != 2) model.addAttribute("tmpgroup", "");
        else model.addAttribute("tmpgroup",devicegroupService.getDevicegroupById(device.getTmpgid()).get().getGname());
        return "device-detail";  // 返回Thymeleaf模板
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Device device = deviceService.getDeviceById(id).orElseThrow(() -> new IllegalArgumentException("Invalid device Id: " + id));
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }
        Siteuser siteuser = siteuserService.findByUname(username);
        if (siteuser.getUgroup() != device.getDgroup() && device.getDgroup() != -1 && siteuser.getUprivi() != 7) {
            model.addAttribute("err","无权访问");
            model.addAttribute("ret","/main");
            return "error";
        }
        int uprivi = siteuser.getUprivi();
        if (uprivi == 0) {
            model.addAttribute("err","无权编辑");
            model.addAttribute("ret","/devices/"+id.toString());
            return "error";
        }
        model.addAttribute("device", device);
        if (siteuser.getUgroup() == -1) model.addAttribute("in_group",0);
        else model.addAttribute("in_group",1);
        model.addAttribute("uprivi", uprivi);
        model.addAttribute("groups", devicegroupService.getAllDevicegroups());
        return "edit-device";  // 返回Thymeleaf模板 'edit-device.html'
    }

    @PostMapping("/edit/{id}")
    public String updateDevice(@PathVariable("id") Integer id, @ModelAttribute("device") Device updatedDevice,
                               @RequestParam("image") MultipartFile imageFile,HttpSession session, HttpServletRequest request, Model model) throws Exception{
        Device device = deviceService.getDeviceById(id).orElseThrow(() -> new IllegalArgumentException("Invalid device Id: " + id));
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }
        Siteuser siteuser = siteuserService.findByUname(username);
        if (siteuser.getUgroup() != device.getDgroup() && device.getDgroup() != -1 && siteuser.getUprivi() != 7) {
            model.addAttribute("err","无权访问");
            model.addAttribute("ret","/main");
            return "error";
        }
        int uprivi = siteuser.getUprivi();
        if (uprivi == 0) {
            model.addAttribute("err","无权编辑");
            model.addAttribute("ret","/devices/"+id.toString());
            return "error";
        }
        if (!imageFile.isEmpty()) {
            // 生成文件名
            String hash = hashTimestampAndFileName(Instant.now().toString() + imageFile.getOriginalFilename());
            String fileName = hash + ".jpg";
            Path filePath = Paths.get("src/main/resources/static/images/" + fileName);

            Files.write(filePath, imageFile.getBytes());

            updatedDevice.setDimage(hash);
        }
        String dprivi = request.getParameter("dprivi");
        if (dprivi.equals("0")) {
            updatedDevice.setDprivi(0);
            updatedDevice.setDgroup(-1);
        }
        else if (uprivi != 7) {
            int ugroup = siteuser.getUgroup();
            updatedDevice.setDprivi(uprivi);
            updatedDevice.setDgroup(ugroup);
        }
        else {
            int pri = Integer.parseInt(dprivi);
            updatedDevice.setDprivi(3);
            updatedDevice.setDgroup(pri);
        }
        if(updatedDevice.getDimage()==null){
            deviceService.getDeviceById(id).ifPresent(_device -> updatedDevice.setDimage(_device.getDimage()));
        }
        if(updatedDevice.getDstate()==null){
            deviceService.getDeviceById(id).ifPresent(_device -> updatedDevice.setDstate(_device.getDstate()));
        }
        if (updatedDevice.getDgroup() == -1) {
            updatedDevice.setTmpgid(0);
            if (updatedDevice.getDstate() == 2) updatedDevice.setDstate(0);
        }
        deviceService.updateDevice(id, updatedDevice);

        return "redirect:/main";  // 重定向到设备列表页面
    }

    @PostMapping("/upload/check")
    public String checkImage(@RequestParam("image") MultipartFile imageFile, HttpSession session, HttpServletRequest request, Model model) throws Exception {
        if(session.getAttribute("username") == null) {
            return "redirect:/";
        }

        String username = session.getAttribute("username").toString();
        Siteuser siteuser = siteuserService.findByUname(username);
        if(siteuser == null) {
            return "redirect:/";
        }
        if (siteuser.getUprivi() == 0) {
            model.addAttribute("err","无权编辑");
            model.addAttribute("ret","/main");
            return "error";
        }
        int dgroup;
        int dprivi;
        String dname = request.getParameter("dname");
        int dtype = Integer.valueOf(request.getParameter("dtype"));
        int dstate = 0;
        String requested_privi = request.getParameter("dprivi");

        if (requested_privi.equals("0")) {
            dgroup = -1;
            dprivi = 0;
        }
        else if (siteuser.getUprivi() != 7){ // 上传到本实验室
            dgroup = siteuser.getUgroup();
            dprivi = siteuser.getUprivi();
        }
        else{
            dgroup = Integer.parseInt(requested_privi);
            dprivi = 3;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date buydate = formatter.parse(request.getParameter("buydate"));

        String detail = request.getParameter("detail");

        Device newdevice = new Device();
        newdevice.setDname(dname);
        newdevice.setDtype(dtype);
        newdevice.setBuydate(buydate);
        newdevice.setDetail(detail);
        newdevice.setDgroup(dgroup);
        newdevice.setDprivi(dprivi);
        newdevice.setDstate(dstate);
        newdevice.setTmpgid(0);

        if (!imageFile.isEmpty()) {
            // 生成文件名
            String hash = hashTimestampAndFileName(Instant.now().toString() + imageFile.getOriginalFilename());
            String fileName = hash + ".jpg";
            Path filePath = Paths.get("src/main/resources/static/images/" + fileName);

            // 保存文件到指定目录
            Files.write(filePath, imageFile.getBytes());

            // 更新设备的dimage字段
            newdevice.setDimage(hash);
        }
        deviceService.saveDevice(newdevice);
        return "redirect:/main";
    }

    @GetMapping("/upload")
    public String uploadDevice(Model model, HttpSession session) {
        if (session == null) return "redirect:/";
        String username = session.getAttribute("username").toString();
        Siteuser siteuser = siteuserService.findByUname(username);
        if(siteuser == null) {
            return "redirect:/";
        }
        int uprivi = siteuser.getUprivi();
        if (uprivi == 0){
            model.addAttribute("err","无权编辑");
            model.addAttribute("ret","/main");
            return "error";
        }
        if (siteuser.getUgroup() == -1) model.addAttribute("in_group",0);
        else model.addAttribute("in_group",1);
        model.addAttribute("uprivi", uprivi);
        model.addAttribute("groups", devicegroupService.getAllDevicegroups());
        return "upload";
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

    @PostMapping("/delete/{id}")
    public String deleteDevice(@PathVariable("id") Integer id, HttpSession session, Model model) {
        if (session == null) return "redirect:/";
        String username = session.getAttribute("username").toString();
        Siteuser siteuser = siteuserService.findByUname(username);
        if(siteuser == null) {
            return "redirect:/";
        }
        Device device = deviceService.getDeviceById(id).orElseThrow(() -> new IllegalArgumentException("Invalid device Id: " + id));
        if (siteuser.getUgroup() != device.getDgroup() && device.getDgroup() != -1 && siteuser.getUprivi() != 7) {
            model.addAttribute("err","无权访问");
            model.addAttribute("ret","/main");
            return "error";
        }
        int uprivi = siteuser.getUprivi();
        if (uprivi == 0){
            model.addAttribute("err","无权编辑");
            model.addAttribute("ret","/main");
            return "error";
        }
        deviceService.deleteDevice(id);
        return "redirect:/main";  // 重定向到设备列表页面
    }
}
