package com.example.checkeventver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.checkeventver2.login_screen.UserData;

public class user_qr extends AppCompatActivity {
    ImageView img;

    String age = "";
    String code = "";
   // String link = "";
    String name = "";
//    String password = "";
//    String role = "";
    String username = "";
    String school = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_qr);


        // Здесь продолжайте использовать полученные данные


        //String role = getIntent().getStringExtra("ROLE");
        String link = getIntent().getStringExtra("LINK");
        name = getIntent().getStringExtra("NAME");
        code = getIntent().getStringExtra("CODE");

        username = getIntent().getStringExtra("USERNAME");
        age = getIntent().getStringExtra("AGE");
        school = getIntent().getStringExtra("SCHOOL");

        img = findViewById(R.id.qr_image);
        Picasso.get().load(link).into(img);

        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String jsonData = data.getQueryParameter("data");
            if (jsonData != null) {
                parseJsonData(jsonData);
            }
        }

        getTexttofield();
    }
    public void getTexttofield(){
        TextView textView = findViewById(R.id.textView_name);
        textView.setText(name);

        TextView textView2 = findViewById(R.id.qr_number_code);
        textView2.setText(code);

        TextView textView3 = findViewById(R.id.user_info);
        textView3.setText(username + "\n" + age + "\n" + school);
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