package com.sample.hanpractice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class NetworkThread extends Thread {

    final private String serviceKey = "LffsTTaENsQZbeP0kR%2Ba6ImEeBcZjELW3TqLdEOh1q6GjOh9TtLA90RkoPps8rQl5mZJ%2BjeiUydmTbfFFShfSw%3D%3D";

    @Override
    public void run() {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapitraffic.daejeon.go.kr/api/rest/busposinfo/getBusPosByRtid"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("busRouteId", "UTF-8") + "=" + URLEncoder.encode("30300001", "UTF-8")); /*노선의고유식별자*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
