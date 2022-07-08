package com.sample.hanpractice.model;

import java.util.ArrayList;

// 전체 버스 목록에 대한 정보를 Singleton 패턴으로 BusData class 생성
public class BusData {
    private ArrayList<BusInfo> bus_info;

    public ArrayList<BusInfo> getBusInfo() {
        return bus_info;
    }
    public void setBusInfo(ArrayList<BusInfo> busInfo) {
        this.bus_info = busInfo;
    }

    private static BusData instance = null;
    public static synchronized BusData getInstance(){
        if(null == instance){
            instance = new BusData();
        }
        return instance;
    }
}
