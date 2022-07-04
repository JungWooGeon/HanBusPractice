package com.sample.hanpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    NetworkThread nThread;
    Button search_button;
    Button advanced_search_button;
    ListView search_result_listview;
    EditText bus_number_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSpinner();
        initButton();

        nThread = new NetworkThread();
        nThread.start();
    }

    private void initViews() {
        search_result_listview = findViewById(R.id.search_result_listview);
        bus_number_edittext = findViewById(R.id.bus_number_edittext);
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
        search_button.setOnClickListener(v -> {
            // TODO 검색 버튼 클릭 시 ListView에 결과 표시 (ListView에 결과가 있을 경우 '상세 검색' 버튼 표시)

            // String text = bus_number_edittext.getText().toString();

            // ListAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
            // search_result_listview.setAdapter(adapter);


        });
        advanced_search_button.setOnClickListener(v -> {
            // TODO 상세 버튼 클릭 이벤트
        });
    }
}