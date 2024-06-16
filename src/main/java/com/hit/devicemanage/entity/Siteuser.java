package com.hit.devicemanage.entity;

import jakarta.persistence.*;

@Entity
public class Siteuser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer uid;

    private String uname;
    private String upasswd;
    private Integer uprivi;
    private Integer ugroup;

    public Siteuser(Integer uid, String uname, String upasswd, Integer uprivi, Integer ugroup) {
        this.uid = uid;
        this.uname = uname;
        this.upasswd = upasswd;
        this.uprivi = uprivi;
        this.ugroup = ugroup;
    }
    public Siteuser() {

    }

    public Integer getUid() { return uid;}
    public void setUid(Integer uid) { this.uid = uid; }
    public String getUname() { return uname; }
    public void setUname(String uname) { this.uname = uname; }
    public String getUpasswd() { return upasswd; }
    public void setUpasswd(String upasswd) { this.upasswd = upasswd; }
    public Integer getUprivi() { return uprivi; }
    public void setUprivi(Integer uprivi) { this.uprivi = uprivi; }
    public Integer getUgroup() { return ugroup; }
    public void setUgroup(Integer ugroup) { this.ugroup = ugroup; }

}
