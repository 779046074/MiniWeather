package com.australian.miniweather.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class WeatherModel {
    @Id(autoincrement = true)
    private long id;
    private String location;
    private String loc;
    private String fl;
    private String cond_txt;
    private String wind_dir;
    private String wind_sc;
    private String wind_spd;
    @Generated(hash = 1413147649)
    public WeatherModel(long id, String location, String loc, String fl,
            String cond_txt, String wind_dir, String wind_sc, String wind_spd) {
        this.id = id;
        this.location = location;
        this.loc = loc;
        this.fl = fl;
        this.cond_txt = cond_txt;
        this.wind_dir = wind_dir;
        this.wind_sc = wind_sc;
        this.wind_spd = wind_spd;
    }
    @Generated(hash = 802490738)
    public WeatherModel() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getLocation() {
        return this.location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getLoc() {
        return this.loc;
    }
    public void setLoc(String loc) {
        this.loc = loc;
    }
    public String getFl() {
        return this.fl;
    }
    public void setFl(String fl) {
        this.fl = fl;
    }
    public String getCond_txt() {
        return this.cond_txt;
    }
    public void setCond_txt(String cond_txt) {
        this.cond_txt = cond_txt;
    }
    public String getWind_dir() {
        return this.wind_dir;
    }
    public void setWind_dir(String wind_dir) {
        this.wind_dir = wind_dir;
    }
    public String getWind_sc() {
        return this.wind_sc;
    }
    public void setWind_sc(String wind_sc) {
        this.wind_sc = wind_sc;
    }
    public String getWind_spd() {
        return this.wind_spd;
    }
    public void setWind_spd(String wind_spd) {
        this.wind_spd = wind_spd;
    }
}
