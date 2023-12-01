package com.example.checkeventver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Click_Registerfirst(View view){
        Intent intent = new Intent(this, register_screen.class);
        startActivity(intent);
    }
    public void Click_Loginfirst(View view){
        Intent intent = new Intent(this, login_screen.class);
        startActivity(intent);
    }

}