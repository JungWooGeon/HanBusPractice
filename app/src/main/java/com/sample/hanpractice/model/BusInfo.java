package com.sample.hanpractice.model;

public class BusInfo {
    private final String bus_node;
    private final String bus_name;
    private final String bus_type;

    public BusInfo(String busNode, String busName, String busType) {
        this.bus_node = busNode;
        this.bus_name = busName;
        this.bus_type = busType;
    }

    public String getBusNode() { return bus_node; }
    public String getBusName() {
        return bus_name;
    }
    public String getBusType() {
        return bus_type;
    }
}
