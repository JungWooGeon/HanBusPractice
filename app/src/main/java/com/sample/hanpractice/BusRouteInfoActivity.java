package com.sample.hanpractice;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sample.hanpractice.model.BusData;
import com.sample.hanpractice.model.BusInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class BusRouteInfoActivity extends AppCompatActivity {
    final private String serviceKey = "서비스키";
    private String busNodeList;
    private String busRouteId = "0";
    private ArrayList<BusInfo> busInfo;

    private BusInfoThread nThread;
    private Button search_button;
    private Button advanced_search_button;
    private TextView search_result_textview;
    private EditText bus_number_edittext;
    private ListView search_bus_listview;
    private ListAdapter search_bus_listview_adapter;
    private final ArrayList<String> busNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route_info);

        initViews();
        initSpinner();
        initButton();
    }

    private void initViews() {
        search_result_textview = findViewById(R.id.search_result_textview);
        bus_number_edittext = findViewById(R.id.bus_number_edittext);

        search_bus_listview = findViewById(R.id.search_bus_listview);
        search_bus_listview_adapter = new ArrayAdapter (this, android.R.layout.simple_list_item_1, busNameList);
        search_bus_listview.setAdapter(search_bus_listview_adapter);
    }

    private void initSpinner() {
        // 지역 목록을 표시하는 Spinner 초기화 (기본 값 : 대전, 서울, 부산)
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
            searchAndSetBusNameList();
        });
        
        // 상세 검색 버튼 클릭 이벤트
        advanced_search_button.setOnClickListener(v -> {
            searchBusStop();
        });
    }

    // 전체 버스목록에 대해서 해당 버스명이 포함된 버스 노선들을 가져와 busNameList에 저장
    private void searchBusName(String busName) {
        busInfo = BusData.getInstance().getBusInfo();
        busNameList.clear();

        for (int i = 0; i < busInfo.size(); i++) {
            String bn = busInfo.get(i).getBusName();
            String type = busInfo.get(i).getBusType().trim();
            if (bn.contains(busName)) {
                switch (type) {
                    case "1":
                        busNameList.add("급행 " +  bn);
                        break;
                    case "2":
                        busNameList.add("간선 " + bn);
                        break;
                    case "3":
                        busNameList.add("지선 " + bn);
                        break;
                    case "4":
                        busNameList.add("외곽 " + bn);
                        break;
                    case "5":
                        busNameList.add("마을 " + bn);
                        break;
                    case "6":
                        busNameList.add("첨단 " + bn);
                        break;
                    default:
                        busNameList.add(bn);
                        break;
                }
            }
        }
        Collections.sort(busNameList);
    }

    // editbox에 있는 버스명이 포함된 버스 노선 ID를 찾고 ListView에 셋팅
    private void searchAndSetBusNameList() {
        // 버스 노선과 버스 ID에 대한 목록 조회 Thread 실행
        try {
            if (nThread == null) {
                nThread = new BusInfoThread();
                nThread.start();
                nThread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        search_result_textview.setText("");
        String busName = bus_number_edittext.getText().toString();
        searchBusName(busName);
        search_bus_listview.setAdapter(search_bus_listview_adapter);

        if (busNameList.size() != 0) {
            advanced_search_button.setVisibility(View.VISIBLE);
        } else {
            advanced_search_button.setVisibility(View.GONE);
        }
    }

    // 버스 노선명에 대한 버스 노선 ID 찾기
    private void searchBusRouteId(String busName) {
        busInfo = BusData.getInstance().getBusInfo();
        // 단순 탐색으로 찾기 -> 더 좋은 방법 있으면 추후에 변경 가능
        busRouteId = "0";
        for (int i = 0; i < busInfo.size(); i++) {
            BusInfo bus = busInfo.get(i);
            if (bus.getBusName().equals(busName)) {
                busRouteId = bus.getBusNode();
                break;
            }
        }
    }

    // 선택된 버스 노선에 대해 모든 정류장을 출력
    private void searchBusStop() {
        // TODO 한소네에서는 editText에 있는 값이 아니라 ListView에서 클릭되어있는 값을 가져와서 진행해야 함
        String busName = bus_number_edittext.getText().toString();
        searchBusRouteId(busName);
        busNodeList = "";

        // 해당 버스 노선에 대한 정류장 목록 조회 후 표시하는 Thread
        Thread searchBusNodeThread  = new Thread(this::busNodeSearch);
        try {
            searchBusNodeThread.start();
            searchBusNodeThread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 버스 노선에 대한 정류장 목록 조회 (API 사용)
    private void busNodeSearch() {
        Thread t = new Thread(() -> {
            try {
                StringBuilder urlBuilder = new StringBuilder("http://openapitraffic.daejeon.go.kr/api/rest/busRouteInfo/getStaionByRoute"); /*URL*/
                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
                urlBuilder.append("&" + URLEncoder.encode("busRouteId", "UTF-8") + "=" + URLEncoder.encode(busRouteId, "UTF-8"));
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
                                case "BUSSTOP_NM":
                                    if (busNodeList.equals("")) {
                                        busNodeList += parser.getText();
                                    } else {
                                        busNodeList += " - " + parser.getText();
                                    }
                                    break;
                            }
                    }
                    //다음으로 이동
                    eventType = parser.next();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
        try {
            t.start();
            t.join();
            runOnUiThread(() -> {
                search_result_textview.setText(busNodeList);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
