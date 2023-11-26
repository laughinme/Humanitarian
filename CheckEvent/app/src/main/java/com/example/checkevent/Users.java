package com.example.checkevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Users extends AppCompatActivity {
    public String username=" ", usereducation= " ";
    public int userage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
    }

    public void Back_Click(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void Send_Click(View view){

        boolean correct=false;
        EditText obj2 = (EditText)findViewById(R.id.editTextText2);
        EditText obj3 = (EditText)findViewById(R.id.editTextText3);
        EditText obj4 = (EditText)findViewById(R.id.editTextText4);
        userage = Integer.parseInt(obj3.getText().toString());
        usereducation = obj2.getText().toString();
        username = obj4.getText().toString();
        if(userage==0 || userage>=150 || username.length()<6 || usereducation.length()<6) {
            correct = false;
        }
        else{
            correct=true;
        }


        if(correct==true)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Ваши данные отправлены!", Toast.LENGTH_SHORT).show();
            //TODO отправлять запрос в бд с переменными userage,username и usereducation
        }
        else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}