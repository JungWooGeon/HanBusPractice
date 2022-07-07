package com.sample.hanpractice;

import com.sample.hanpractice.model.BusData;
import com.sample.hanpractice.model.BusInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BusInfoThread extends Thread {

    final private String serviceKey = "LffsTTaENsQZbeP0kR%2Ba6ImEeBcZjELW3TqLdEOh1q6GjOh9TtLA90RkoPps8rQl5mZJ%2BjeiUydmTbfFFShfSw%3D%3D";
    private final ArrayList<BusInfo> busInfo = new ArrayList<BusInfo>();
    private String busNode;
    private String busName;
    private String busType;

    @Override
    public void run() {
        // busNode id 정보 확인
        for (int i = 1; i <= 2; i++) {
            busInfoSearch(Integer.toString(i));
        }
        BusData.getInstance().setBusInfo(busInfo);
    }

    private void busInfoSearch(String pageNumber) {
        try {
            StringBuilder urlBuilder = new StringBuilder("http://openapitraffic.daejeon.go.kr/api/rest/busRouteInfo/getRouteInfoAll"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("reqPage", "UTF-8") + "=" + URLEncoder.encode(pageNumber, "UTF-8"));
            URL url = new URL(urlBuilder.toString());

            XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xmlPullParserFactory.newPullParser();

            InputStream is = url.openStream();
            parser.setInput(new InputStreamReader(is, "UTF-8"));
            String tagName = "";

            //event type얻어오기
            int eventType = parser.getEventType();

            //xml문서의 끝까지 읽기
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        break;

                    //태그 안의 텍스트
                    case XmlPullParser.TEXT:
                        switch (tagName) {
                            case "ROUTE_CD":
                                busNode = parser.getText();
                                break;
                            case "ROUTE_NO":
                                busName = parser.getText();
                                break;
                            case "ROUTE_TP":
                                busType = parser.getText();
                                BusInfo bus = new BusInfo(busNode, busName, busType);
                                busInfo.add(bus);
                                break;
                        }
                }
                //다음으로 이동
                eventType = parser.next();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
