package com.sample.hanpractice;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class BusArriveInfoActivity extends AppCompatActivity {
    BusInfoThread nThread;
    Button search_button;
    Button advanced_search_button;
    ListView search_result_listview;
    EditText bus_number_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_arrive_info);

        initViews();
        initSpinner();
        initButton();

        nThread = new BusInfoThread();
        nThread.start();
    }

    private void initViews() {
        search_result_listview = findViewById(R.id.search_result_listview);
        bus_number_edittext = findViewById(R.id.bus_stop_edittext);
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
    }
}
