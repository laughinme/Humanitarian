package com.example.checkeventver2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.Serializable;


public class login_screen extends AppCompatActivity {


    public class UserData implements Serializable {
        private String username;
        private String password;
        private String name;
        private String age;
        private String school;
        private String role;
        private String code;
        private  String link;

        // Конструктор
        public UserData() {
        }


        // Геттеры и сеттеры
        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void Click_Register(View view) {
        Intent intent = new Intent(this, register_screen.class);
        startActivity(intent);
    }

    public void Click_Login(View view) {
        EditText editText = findViewById(R.id.login_example_email);
        String gmail = editText.getText().toString();
        EditText editText2 = findViewById(R.id.login_example_password);
        String password = editText2.getText().toString();
        sendRegisterRequest(gmail, password);
    }

    public void sendRegisterRequest(String gmail, String password) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://us.pylex.me:8677/v1/users/login").newBuilder();
        urlBuilder.addQueryParameter("username", gmail);
        urlBuilder.addQueryParameter("password", password);


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
                    if (response.code() == 404) {
                        handle404();
                    } else if (response.code() == 401) {
                        handle401();
                    }
                } else {
                    String responseData = response.body().string();
                    String username = null;
                    String password = null;
                    String name = null;
                    String age = null;
                    String school = null;
                    String role = null;
                    String code = null;
                    String link = null;

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        username = jsonObject.optString("username");
                        password = jsonObject.optString("password");
                        name = jsonObject.optString("name");
                        age = jsonObject.optString("age");
                        school = jsonObject.optString("school");
                        role = jsonObject.optString("role");
                        code = jsonObject.optString("code");
                        link = jsonObject.optString("link");
                        handle200(username, password, name, age, school, code, role, link);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }

            private void handle401() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(login_screen.this, "Неверное имя пользователя или пароль ", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            private void handle404() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(login_screen.this, "Такого пользователя не существует", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private void handle200(String username, String password, String name, String age, String school, String code, String role, String link) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = null;
                        if ("admin".equals(role)) {
                            intent = new Intent(login_screen.this, admin_panel.class);
                            startActivity(intent);
                        } else if ("user".equals(role)) {
                            intent = new Intent(login_screen.this, user_qr.class);
                            startActivity(intent);
                        } else if ("checker".equals(role)) {
                            intent = new Intent(login_screen.this, checker_qr.class);
                            startActivity(intent);
                        }


                        intent.putExtra("ROLE", role);
                        intent.putExtra("USERNAME", username);
                        intent.putExtra("NAME", name);
                        intent.putExtra("SCHOOL", school);
                        intent.putExtra("PASSWORD", password);
                        intent.putExtra("CODE", code);
                        intent.putExtra("LINK", link);
                        intent.putExtra("AGE", age);

                        startActivity(intent);
                    }
                });
            }
        });
    }
}


