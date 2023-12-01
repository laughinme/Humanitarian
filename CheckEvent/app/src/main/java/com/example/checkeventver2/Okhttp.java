package com.example.checkeventver2;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class Okhttp {

    public static void main(String[] args) {
        fetchUserData("531235");

    }

    public static void fetchUserData(String code) {
        OkHttpClient client = new OkHttpClient();

        // Создание нового потока для выполнения сетевого запроса
        new Thread(() -> {
            try {
                Request request = new Request.Builder()
                        .url("http://us.pylex.me:8677/user?code=" + code)
                        .build();

                Response response = client.newCall(request).execute();
                String responseData = response.body().string();

                parseJSON(responseData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void parseJSON(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String message = jsonObject.getString("message");
            String username = jsonObject.getString("username");
            String name = jsonObject.getString("name");
            int age = jsonObject.getInt("age");
            String school = jsonObject.getString("school");

            // Вывод данных на консоль (или другая логика обработки данных)
            System.out.println("Message: " + message);
            System.out.println("Username: " + username);
            System.out.println("Name: " + name);
            System.out.println("Age: " + age);
            System.out.println("School: " + school);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
