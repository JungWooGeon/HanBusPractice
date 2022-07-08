package com.sample.hanpractice;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.sample.hanpractice.model.BusArriveTimeInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Array;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BusArriveInfoActivity extends AppCompatActivity {
    final private String serviceKey = "LffsTTaENsQZbeP0kR%2Ba6ImEeBcZjELW3TqLdEOh1q6GjOh9TtLA90RkoPps8rQl5mZJ%2BjeiUydmTbfFFShfSw%3D%3D";
    final private String cityCode = "25";

    private Button search_button;
    private Button advanced_search_button;
    private ListView search_bus_stop_listview;
    private EditText bus_number_edittext;
    private ListAdapter search_bus_stop_listview_adapter;
    private ListView search_bus_arrive_listview;
    private ListAdapter search_bus_arrive_listview_adapter;

    private ArrayList<String> busStopNameList = new ArrayList<String>();
    private ArrayList<String> busIdWithBusStopNameList = new ArrayList<String>();
    private ArrayList<BusArriveTimeInfo> busArriveResultList = new ArrayList<BusArriveTimeInfo>();
    ArrayList<String> busArriveList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_arrive_info);

        initViews();
        initSpinner();
        initButton();

        // CertPathValidatorException 오류가 발생하여 추가
        trustAllHosts();
    }

    private void initViews() {
        bus_number_edittext = findViewById(R.id.bus_stop_edittext);
        search_bus_stop_listview = findViewById(R.id.search_bus_stop_listview);
        search_bus_stop_listview_adapter = new ArrayAdapter (this, android.R.layout.simple_list_item_1, busStopNameList);
        search_bus_stop_listview.setAdapter(search_bus_stop_listview_adapter);

        search_bus_arrive_listview = findViewById(R.id.search_bus_arrive_listview);
        search_bus_arrive_listview_adapter =  new ArrayAdapter (this, android.R.layout.simple_list_item_1, busArriveList);
        search_bus_arrive_listview.setAdapter(search_bus_arrive_listview_adapter);
    }

    private void initSpinner() {
        Spinner busInfoComboBox = findViewById(R.id.bus_info_spinner);
        ArrayAdapter<CharSequence> busInfoComboBoxAdapter = ArrayAdapter.createFromResource(this, R.array.infoList, android.R.layout.simple_spinner_item);
        busInfoComboBoxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        busInfoComboBox.setAdapter(busInfoComboBoxAdapter);
    }

    private void initButton() {
        search_button = findViewById(R.id.search_button);
        advanced_search_button = findViewById(R.id.advanced_search_button);

        // 검색 버튼 클릭 이벤트
        search_button.setOnClickListener(v -> {
            searchBusStopName();
        });

        // 상세 검색 버튼 클릭 이벤트
        advanced_search_button.setOnClickListener(v -> {
            search_bus_arrive_time();
        });
    }

    private void searchBusStopName() {
        clearSearchBusArriveListview();
        clearSearchBusStopListview();
        advanced_search_button.setVisibility(View.GONE);

        Thread busInfoThread = new Thread(() -> {
            String busStopName = bus_number_edittext.getText().toString().trim();
            String bus_stop_id = "";
            String bus_stop_name = "";
            try {
                StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1613000/BusSttnInfoInqireService/getSttnNoList"); /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=" + URLEncoder.encode(cityCode, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("nodeNm", "UTF-8") + "=" + URLEncoder.encode(busStopName, "UTF-8"));
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
                                case "nodenm":
                                    bus_stop_name = parser.getText();
                                    break;
                                case "nodeno":
                                    bus_stop_id = parser.getText();
                                    busStopNameList.add(bus_stop_name);
                                    busIdWithBusStopNameList.add(bus_stop_id);
                                    break;
                            }
                    }
                    //다음으로 이동
                    eventType = parser.next();
                }

                runOnUiThread(() -> {
                    advanced_search_button.setVisibility(View.VISIBLE);
                    search_bus_stop_listview.setAdapter(search_bus_stop_listview_adapter);
                });
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        busInfoThread.start();
    }


    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search_bus_arrive_time() {
        // TODO 임의로 List에 첫 번째 있는 정류장으로 검색하여 표시하도록 하였으니 ListView에서 선택된 값으로 바꾸어주어야 함

        clearSearchBusArriveListview();

        Thread t = new Thread(() -> {
            String arrive_time = "";
            String bus_name = "";
            String bus_type = "";

            try {
                StringBuilder urlBuilder = new StringBuilder("http://openapitraffic.daejeon.go.kr/api/rest/arrive/getArrInfoByUid"); /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("arsId", "UTF-8") + "=" + URLEncoder.encode(busIdWithBusStopNameList.get(0), "UTF-8"));
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
                                case "EXTIME_MIN":
                                    arrive_time = parser.getText();
                                    break;
                                case "ROUTE_NO":
                                    bus_name = parser.getText();
                                    break;
                                case "ROUTE_TP":
                                    bus_type = parser.getText();
                                    BusArriveTimeInfo busArriveTimeInfo = new BusArriveTimeInfo(arrive_time, bus_name, bus_type);
                                    busArriveResultList.add(busArriveTimeInfo);
                                    break;
                            }
                    }
                    //다음으로 이동
                    eventType = parser.next();
                }
                runOnUiThread(this::setSearchBusArriveListview);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        t.start();
    }

    private void clearSearchBusArriveListview() {
        busArriveList.clear();
        busArriveResultList.clear();
        search_bus_arrive_listview.setAdapter(search_bus_arrive_listview_adapter);
    }

    private void clearSearchBusStopListview() {
        busStopNameList.clear();
        busIdWithBusStopNameList.clear();
        search_bus_stop_listview.setAdapter(search_bus_stop_listview_adapter);
    }

    private void setSearchBusArriveListview() {
        for (int i = 0; i < busArriveResultList.size(); i++) {
            String x = "";
            x += busArriveResultList.get(i).getBus_name() + "\t";
            switch (busArriveResultList.get(i).getBus_type()) {
                case "1":
                    x += ("급행\t");
                    break;
                case "2":
                    x += ("간선\t");
                    break;
                case "3":
                    x += ("지선\t");
                    break;
                case "4":
                    x += ("외곽\t");
                    break;
                case "5":
                    x += ("마을\t");
                    break;
                case "6":
                    x += ("첨단\t");
                    break;
                default:
                    break;
            }

            x += busArriveResultList.get(i).getArrive_time();
            busArriveList.add(x);
        }

        search_bus_arrive_listview_adapter =  new ArrayAdapter (this, android.R.layout.simple_list_item_1, busArriveList);
        search_bus_arrive_listview.setAdapter(search_bus_arrive_listview_adapter);
    }
}
