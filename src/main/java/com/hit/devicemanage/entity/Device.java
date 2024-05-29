package com.hit.devicemanage.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long did;
    private String dname;
    private Integer dtype;
    private String dimage;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buydate;
    private String detail;
    private Integer dgroup;
    private Integer dprivi;

    public Device(Long did, String dname, Integer dtype, String dimage, Date buydate, String detail, Integer dgroup, Integer dprivi) {
        this.did = did;
        this.dname = dname;
        this.dtype = dtype;
        this.dimage = dimage;
        this.buydate = buydate;
        this.detail = detail;
        this.dgroup = dgroup;
        this.dprivi = dprivi;
    }

    public Device() {

    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
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

    public String getFormattedBuydate() {
        if (this.buydate != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(buydate);
        }
        return "";
    }
}
