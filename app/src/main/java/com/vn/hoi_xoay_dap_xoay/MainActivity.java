package com.vn.hoi_xoay_dap_xoay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn_batDau(View view) {
        String Title = "Mày Nghĩ M Vui Không \n   Vào Chiến Thôi";
        Toast.makeText(this,Title  , Toast.LENGTH_SHORT).show();

    }
}