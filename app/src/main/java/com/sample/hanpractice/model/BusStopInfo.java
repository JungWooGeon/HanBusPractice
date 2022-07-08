package com.sample.hanpractice.model;

public class BusStopInfo {
    private final String bus_stop_id;
    private final String bus_stop_name;

    public BusStopInfo (String bus_stop_id, String bus_stop_name) {
        this.bus_stop_id = bus_stop_id;
        this.bus_stop_name = bus_stop_name;
    }

    public String getBusStopId() {
        return this.bus_stop_id;
    }
    public String getBus_stop_name() {
        return this.bus_stop_name;
    }
}
