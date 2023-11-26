package com.example.checkevent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CheckUsersCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_users_code);
    }
    public void Click_Check(View view){
        String key;
        EditText obj = (EditText)findViewById(R.id.editTextText);
        key = obj.getText().toString();
        if(key.length()>=5) {
            //TODO сопоставть key с ключом в бд
        }
        else {
            Toast.makeText(this, "Введите корректный ключ", Toast.LENGTH_SHORT).show();
        }
    }
}