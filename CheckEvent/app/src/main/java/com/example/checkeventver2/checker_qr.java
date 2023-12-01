package com.example.checkeventver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class checker_qr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker_qr);
    }

    public void Click_Check(View view){
        Intent intent = new Intent();
        startActivity(intent);
    }
}