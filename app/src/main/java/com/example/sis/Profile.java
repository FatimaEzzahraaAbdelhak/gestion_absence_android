package com.example.sis;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {
    private SharedPreferences pref;
    private NetworkUtils networkUtils;
    private User user;

    private TextView full_name_ar;
    private TextView full_name_en;
    private TextView program_name_ar;
    private TextView program_name_en;
    private EditText email;
    private TextView civilID;
    private EditText mobile;
    private TextView adresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        user = new User(pref.getString("UserLogin", null ) );

        ActionBar actionBar = this.getSupportActionBar();
        String classTitle = actionBar.getTitle().toString();



        full_name_ar = findViewById(R.id.full_name_ar);
        full_name_en = findViewById(R.id.full_name_en);
        program_name_ar = findViewById(R.id.program_name_ar);
        program_name_en = findViewById(R.id.program_name_en);
        email = findViewById(R.id.email);
        civilID = findViewById(R.id.civilID);
        mobile = findViewById(R.id.mobile);
        adresse = findViewById(R.id.adresse);

        full_name_ar.setText(user.getNom());
        full_name_en.setText(user.getPrenom());
        program_name_ar.setText(user.getNom());
        program_name_en.setText(user.getPrenom());
        email.setText(user.getEmail());
        civilID.setText(user.getCodepostal());
        mobile.setText(user.getCodepostal());
        adresse.setText( user.getAdresse() );
    }

    public void onOptionsItemSelected(View item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
    }

    public void update(View item){

        networkUtils = new NetworkUtils();
        APIEndPoint apiEndPoint = networkUtils.getApiEndPoint();

        Call<ResponseBody> call = apiEndPoint.Editeprofile(
                "application/json",
                user.getId(),
                email.getText().toString(),
                mobile.getText().toString(),
                adresse.getText().toString()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String msg = "please try again ...";
                if (response.code() == 200) {
                    msg = "Done ...";


                    user.setEmail( email.getText().toString() );
                    user.setCin( mobile.getText().toString() );
                    user.setAdresse( adresse.getText().toString() );

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("UserLogin",user.toString());
                    editor.commit();

                }
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", "MSG " + t.getMessage());
                Toast.makeText(getApplicationContext(),"please try again ...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
