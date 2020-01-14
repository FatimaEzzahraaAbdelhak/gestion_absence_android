package com.example.sis;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {
    private SharedPreferences pref;

    User user;
    private TextView errorText;
    private EditText username;
    private EditText password;
    private NetworkUtils networkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        if( pref.contains("UserLogin") ){
            openMain();
        }

        networkUtils = new NetworkUtils();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        errorText = findViewById(R.id.error_text);


    }

    public void submit(View view) {
        errorText.setText( "" );
        APIEndPoint apiEndPoint = networkUtils.getApiEndPoint();

        Call<ResponseBody> call = apiEndPoint.login("application/json", username.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        Document document = Jsoup.parse(response.body().string());
                        Element p = document.select("body").first();
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<User>() {
                        }.getType();

                        user = gson.fromJson(p.text(), collectionType);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("UserLogin", p.text());
                        editor.commit();

                        openMain();
                    } else if (response.code() == 404) {
                        user = null;
                        errorText.setText( "Failed to login, please check your ID or password" );
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    errorText.setText( "Failed : " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", "MSG " + t.getMessage());
                errorText.setText( "Failed : " + t.getMessage());
            }
        });
    }

    public void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
