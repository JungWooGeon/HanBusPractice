package com.sample.hanpractice.model;

import android.app.Application;
import java.util.ArrayList;

public class BusData {
    private ArrayList<BusInfo> busInfo;

    public ArrayList<BusInfo> getBusInfo() {
        return busInfo;
    }
    public void setBusInfo(ArrayList<BusInfo> busInfo) {
        this.busInfo = busInfo;
    }

    private static BusData instance = null;
    public static synchronized BusData getInstance(){
        if(null == instance){
            instance = new BusData();
        }
        return instance;
    }
}
