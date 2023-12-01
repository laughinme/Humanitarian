package com.example.checkeventver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class show_user_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_info);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();


        if (data != null) {
            String jsonData = data.getQueryParameter("data");
            if (jsonData != null) {
                parseJsonData(jsonData);
            }
        }
    }

    private void parseJsonData(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String username = jsonObject.getString("username");
            String name = jsonObject.getString("name");
            int age = jsonObject.getInt("age");
            String school = jsonObject.getString("school");
            String role = jsonObject.getString("role");
            int code = jsonObject.getInt("code");
            Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}