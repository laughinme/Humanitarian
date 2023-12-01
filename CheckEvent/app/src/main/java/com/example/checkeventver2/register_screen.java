package com.example.checkeventver2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import org.json.JSONObject;
import java.io.IOException;

public class register_screen extends AppCompatActivity {
    public String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

    }
    //TODO: сделать перенос от кнопки Click_Alreadyhaveaccount на экран логина

    public void Click_Register3(View view) {
        EditText editText = findViewById(R.id.reg_example_name);
        String name = editText.getText().toString();
        EditText editText2 = findViewById(R.id.reg_example_email);
        String email = editText2.getText().toString();
        EditText editText3 = findViewById(R.id.reg_example_password);
        String password = editText3.getText().toString();
        EditText editText4 = findViewById(R.id.reg_example_school);
        String school = editText4.getText().toString();
        EditText editText5 = findViewById(R.id.reg_example_age);
        String age = editText5.getText().toString();

        sendRegisterRequest(email, password, age, school, name);
    }

    public void sendRegisterRequest(String username, String password, String age, String school, String name) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://us.pylex.me:8677/v1/users/register").newBuilder();
        urlBuilder.addQueryParameter("username", username);
        urlBuilder.addQueryParameter("password", password);
        urlBuilder.addQueryParameter("age", age);
        urlBuilder.addQueryParameter("school", school);
        urlBuilder.addQueryParameter("name", name);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null)) // Пустое тело для POST-запроса
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("HTTP Error", "Ошибка отправки запроса", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (response.code() == 302) {
                        handleRedirectResponse();
                    }

                }
                else {
                    handleintent();
                }

            }

            private void handleRedirectResponse() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(register_screen.this, "Такой пользователь уже существует ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(register_screen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
            private void handleintent() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(register_screen.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }


        });
    }

    private void handleServerResponse(String responseData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    String message = jsonObject.getString("message");

                    if (jsonObject.has("code")) {
                        String code = jsonObject.getString("code");
                        String role = jsonObject.getString("role");
                        // Действия в случае успешной регистрации
                    }
                    else if (jsonObject.has("username")) {
                        String username = jsonObject.getString("username");
                        // Действия, если пользователь существует
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Класс данных для регистрации
    static class Register_data {
        public String name;
        public String email;
        public String password;
        public String school;
        public String age;
    }


    public void Click_Alreadyhaveaccount(View view)
    {
        Intent intent = new Intent(this, login_screen.class);
        startActivity(intent);
    }
}