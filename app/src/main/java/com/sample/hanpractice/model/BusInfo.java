package com.sample.hanpractice.model;

public class BusInfo {
    private final String busNode;
    private final String busName;
    private final String busType;

    public BusInfo(String busNode, String busName, String busType) {
        this.busNode = busNode;
        this.busName = busName;
        this.busType = busType;
    }

    public String getBusNode() { return busNode; }
    public String getBusName() {
        return busName;
    }
    public String getBusType() {
        return busType;
    }
}
