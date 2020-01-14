package com.example.sis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AjouterEtudiant extends AppCompatActivity {

    private SharedPreferences pref;
    private NetworkUtils networkUtils;
    private User user;

    private EditText cne;
    private EditText cin;
    private EditText nom;
    private EditText prenom;
    private EditText ville;
    private EditText email;
    private EditText password;
    private EditText adresse;
    private EditText codepostal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_etudiant);

        cne = findViewById(R.id.cne);
        cin = findViewById(R.id.cin);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        adresse = findViewById(R.id.adresse);
        codepostal = findViewById(R.id.codepostal);
    }


    public void enregistrer(View view){

        networkUtils = new NetworkUtils();
        APIEndPoint apiEndPoint = networkUtils.getApiEndPoint();

        Call<ResponseBody> call = apiEndPoint.ajouter_etudient(
                "application/json",
                cne.getText().toString(),
                password.getText().toString(),
                cin.getText().toString(),
                nom.getText().toString(),
                prenom.getText().toString(),
                email.getText().toString(),
                ville.getText().toString(),
                adresse.getText().toString(),
                codepostal.getText().toString()
        );
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String msg = "Veuillez réessayer ...";
                if (response.code() == 200) {
                    msg = "Bien Enregistrer ...";
                    cne.setText("");
                    password.setText("");
                    cin.setText("");
                    nom.setText("");
                    prenom.setText("");
                    email.setText("");
                    ville.setText("");
                    adresse.setText("");
                    codepostal.setText("");
                }
                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", "MSG " + t.getMessage());
                Toast.makeText(getApplicationContext(),"Veuillez réessayer ...", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
