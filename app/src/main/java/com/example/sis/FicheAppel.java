package com.example.sis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class FicheAppel extends AppCompatActivity {

    private SharedPreferences pref;
    private NetworkUtils networkUtils;
    APIEndPoint apiEndPoint;
    private User user;

    private ListView list_view_etuiants;
    private MyCustomAdapter myadpter;
    private ArrayList<Etudient_abs> etudiants_abs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche_appel);


        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        user = new User(pref.getString("UserLogin", null ) );

        networkUtils = new NetworkUtils();
        apiEndPoint = networkUtils.getApiEndPoint();
        list_view_etuiants = findViewById(R.id.list_view_etuiants);

        load_liste_etudiants();
    }


    public void load_liste_etudiants(){
        Call<ResponseBody> call = apiEndPoint.liste_etudiants(
                "application/json",
                ""
        );
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        Document document = Jsoup.parse(response.body().string());
                        Element p = document.select("p").first();
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<ArrayList<Etudient_abs>>(){}.getType();

                        etudiants_abs = gson.fromJson(p.text(), collectionType);

                        myadpter = new MyCustomAdapter(etudiants_abs);
                        list_view_etuiants.setAdapter(myadpter);
                        myadpter.notifyDataSetChanged();

                    } else if (response.code() == 404) {
                        Toast.makeText(getApplicationContext(),"Aucun resultat", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", "MSG " + t.getMessage());
            }
        });

        list_view_etuiants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                try {
                    final TextView et = view.findViewById(R.id.etud_etat);

                    AlertDialog.Builder builder = new AlertDialog.Builder(FicheAppel.this);
                    builder.setMessage("Merci de choisir l'état")
                            .setIcon(R.drawable.icon_back)
                            .setTitle("Absence Titre")
                            .setPositiveButton("Présent", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    etudiants_abs.get(position).etat = "Présent";
                                    et.setText("Présent");
                                    et.setBackgroundColor( getResources().getColor( R.color.colorBlue) );
                                }
                            })
                            .setNegativeButton("Absent", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    etudiants_abs.get(position).etat = "Absent";
                                    et.setText("Absent");
                                    et.setBackgroundColor( getResources().getColor( R.color.colorRed) );
                                }
                            })
                            .setNeutralButton("Excuse", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    etudiants_abs.get(position).etat = "Excuse";
                                    et.setText("Excuse");
                                    et.setBackgroundColor( getResources().getColor( R.color.colorAccent) );
                                }
                            })
                            .show();
                } catch (Exception e) {
                }
            }
        });
    }


    public void submit_absence( View ciew ){
        String data = "__";
        TextView txt_id;
        TextView txt_etat;
        for ( Etudient_abs et_abs : etudiants_abs ){
            if( et_abs.etat.equals("Absent") || et_abs.etat.equals("Excuse") )
                data += ", ("+et_abs.id + ",'" + et_abs.etat + "') ";
        }

        if( !data.equals("__") ){

            Call<ResponseBody> call = apiEndPoint.submit_absence(
                    "application/json",
                    data
            );
            call.enqueue(new Callback<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String msg = "Bien Valider ....";
                    if (response.code() != 200) {
                        msg = "Try again ....";
                    }
                    Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("onFailure", "MSG " + t.getMessage());
                }
            });
        }


    }






    /*------------------------------*/

    class Etudient_abs{
        public String id;
        public String nom;
        public String etat;

        public Etudient_abs(String i, String n, String e){
            this.id = i;
            this.nom =n;
            this.etat =e;
        }

    }

    class MyCustomAdapter extends BaseAdapter {
        ArrayList<Etudient_abs> Items = new ArrayList<Etudient_abs>();

        MyCustomAdapter(ArrayList<Etudient_abs> itms) {
            this.Items = itms;
        }
        @Override
        public int getCount() {
            return this.Items.size();
        }

        @Override
        public String getItem(int position) {
            return this.Items.get(position).id;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            LayoutInflater linflater = getLayoutInflater();
            View v = linflater.inflate(R.layout.liste_etudiants_item, null);

            TextView etud_id = (TextView) v.findViewById(R.id.etud_id);
            TextView etud_nom = (TextView) v.findViewById(R.id.etud_nom);

            etud_id.setText( this.Items.get(i).id );
            etud_nom.setText( this.Items.get(i).nom );

            return v;
        }
    }

}
