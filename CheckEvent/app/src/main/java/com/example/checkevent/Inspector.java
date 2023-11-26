package com.example.checkevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Inspector extends AppCompatActivity {
    public int password = 3202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector);
    }

    public void Back_Click(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void CheckInspector(View view){
        EditText getpasobj = (EditText)findViewById(R.id.editTextNumberPassword2);
        int getpas = Integer.parseInt(getpasobj.getText().toString());
        if(getpas == password)
        {
            Intent intent = new Intent(this, CheckUsersCode.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Пароль введён неверно", Toast.LENGTH_SHORT).show();
        }
    }
}