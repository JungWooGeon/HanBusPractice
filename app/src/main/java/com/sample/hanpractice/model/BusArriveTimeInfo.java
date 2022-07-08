package com.sample.hanpractice.model;

public class BusArriveTimeInfo {
    private String arrive_time;
    private String bus_name;
    private String bus_type;

    public BusArriveTimeInfo(String arrive_time, String bus_name, String bus_type) {
        this.arrive_time = arrive_time;
        this.bus_name = bus_name;
        this.bus_type = bus_type;
    }

    public String getArrive_time() { return this.arrive_time; }
    public String getBus_name() { return this.bus_name; }
    public String getBus_type() { return this.bus_type; }
}
