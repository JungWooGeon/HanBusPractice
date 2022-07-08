package com.sample.hanpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bus_info_button;
    Button bus_arrive_info_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButton();
    }

    private void initButton() {
        bus_info_button = (Button) findViewById(R.id.bus_info_button);
        bus_arrive_info_button = (Button) findViewById(R.id.bus_arrive_info_button);

        bus_info_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BusRouteInfoActivity.class);
            startActivity(intent);
        });
        bus_arrive_info_button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BusArriveInfoActivity.class);
            startActivity(intent);
        });
    }
}