package com.vn.hoi_xoay_dap_xoay.anim;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vn.hoi_xoay_dap_xoay.DataBase;
import com.vn.hoi_xoay_dap_xoay.MainActivity;
import com.vn.hoi_xoay_dap_xoay.R;

public class CreenStart extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creenstart);
        DataBase dataBase = new DataBase(this);
        AddEventChange();
    }

    private void AddEventChange() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(CreenStart.this, MainActivity.class);
                startActivity(i);
            }
        },4000);
    }
}
