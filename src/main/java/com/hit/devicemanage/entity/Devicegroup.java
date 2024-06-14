package com.hit.devicemanage.entity;

import jakarta.persistence.*;

@Entity
public class Devicegroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long gid;

    private String gname;
    private String gcode;

    public Devicegroup(Long gid, String gname, String gcode) {
        this.gid = gid;
        this.gname = gname;
        this.gcode = gcode;
    }

    public Devicegroup() {

    }

    public Long getGid() {
        return gid;
    }
    public void setGid(Long gid) {
        this.gid = gid;
    }
    public String getGname() {
        return gname;
    }
    public void setGname(String gname) {
        this.gname = gname;
    }
    public String getGcode() {
        return gcode;
    }
    public void setGcode(String gcode) {
        this.gcode = gcode;
    }

}
