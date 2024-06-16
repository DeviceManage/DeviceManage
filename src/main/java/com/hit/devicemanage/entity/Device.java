package com.hit.devicemanage.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer did;
    private String dname;
    private Integer dtype;
    private String dimage;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buydate;
    private String detail;
    private Integer dgroup;
    private Integer dprivi;
    private Integer dstate;

    public Device(Integer did, String dname, Integer dtype, String dimage, Date buydate, String detail, Integer dgroup, Integer dprivi, Integer dstate) {
        this.did = did;
        this.dname = dname;
        this.dtype = dtype;
        this.dimage = dimage;
        this.buydate = buydate;
        this.detail = detail;
        this.dgroup = dgroup;
        this.dprivi = dprivi;
        this.dstate = dstate;
    }

    public Device() {

    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Integer getDtype() {
        return dtype;
    }

    public void setDtype(Integer dtype) {
        this.dtype = dtype;
    }

    public Date getBuydate() {
        return buydate;
    }

    public void setBuydate(Date buydate) {
        this.buydate = buydate;
    }

    public String getDimage() {
        return dimage;
    }

    public void setDimage(String dimage) {
        this.dimage = dimage;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getDgroup() { return dgroup; }

    public void setDgroup(Integer dgroup) { this.dgroup = dgroup; }

    public Integer getDprivi() { return dprivi; }

    public void setDprivi(Integer dprivi) { this.dprivi = dprivi; }

    public Integer getDstate() { return dstate; }

    public void setDstate(Integer dstate) { this.dstate = dstate; }


    public String getFormattedBuydate() {
        if (this.buydate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(buydate);
        }
        return "";
    }

    public String getTypeName()
    {
        if (dtype == 0) {
            return "设备";
        } else if (dtype == 1) {
            return "家具";
        } else if (dtype == 2) {
            return "其他";
        }
        return "未知";
    }
}
