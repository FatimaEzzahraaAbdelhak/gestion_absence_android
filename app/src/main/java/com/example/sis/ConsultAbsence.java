package com.example.sis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class ConsultAbsence extends AppCompatActivity {

    private NetworkUtils networkUtils;
    APIEndPoint apiEndPoint;
    private Spinner spinner_dates;
    private ArrayList<String> dates;
    private WebView wbv;
    private WebView wbv_printer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_absence);



        wbv = findViewById(R.id.wbv);
        wbv.getSettings().setBuiltInZoomControls(true);
        wbv_printer = findViewById(R.id.wbv_printer);

        networkUtils = new NetworkUtils();
        apiEndPoint = networkUtils.getApiEndPoint();
        spinner_dates = findViewById(R.id.spiner_dates);



        Call<ResponseBody> call = apiEndPoint.get_absences(
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
                        Element data_dates = document.select("p").first();
                        Gson gson = new Gson();

                        Type collectionType2 = new TypeToken<ArrayList<String>>(){}.getType();
                        dates = gson.fromJson(data_dates.text(), collectionType2);

                        if( spinner_dates.getAdapter() == null ){
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConsultAbsence.this,
                                    android.R.layout.simple_spinner_item, dates);
                            spinner_dates.setAdapter(adapter);
                        }
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


        spinner_dates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wbv.loadDataWithBaseURL("file:///android_asset/", "", "text/html", "UTF-8", null);

                String d = parent.getItemAtPosition(position).toString();
                get_absences__(d);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void get_absences__(String date_){


        Call<ResponseBody> call = apiEndPoint.get_absences(
                "application/json",
                date_
        );
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String html="";
                    if (response.code() == 200) {
                        Document document = Jsoup.parse(response.body().string());
                        Element abs_data = document.select("p").first();
                        Gson gson = new Gson();

                        Type collectionType = new TypeToken<ArrayList<absence>>(){}.getType();
                        ArrayList<absence> absences = gson.fromJson(abs_data.text(), collectionType);

                        html += "<table class='table_' cellspacing='0' cellpadding='0' >";
                        html += "<thead>";
                        html += "<tr>";
                        html += "<td>Etudiant</td>";
                        html += "<td>Etat</td>";
                        html += "</tr>";
                        html += "</thead>";

                        html += "<tbody>";
                        int i =0;
                        for( absence a : absences ){

                            html += "<tr class='cls_"+i+"'>";
                            html += "<td class='etudient'>"+a.etudeint+"</td>";
                            html += "<td  class='etat "+a.etat+"'>"+a.etat+"</td>";
                            html += "</tr>";

                            if(i== 0)
                                i=1;
                            else
                                i=0;

                        }
                        html += "</tbody>";
                        html += "</table>";
                        html +="<style>table td{padding: 10px; } table thead tr td,table tfoot tr td{background: #3465A4; color: #fff; } table tbody tr.cls_0 td{background: #eee; }table tbody tr td.etudient{width: 100% }table tbody tr td.etat{width: 30% }table tbody tr td.Absent{background-color: #f00 }table tbody tr td.Excuse{background-color: #FF9800 }</style>";

                    } else if (response.code() == 404) {
                        html = "<br><h3>Aucune Resultat ....</h3>";
                    }


                    wbv.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
                    wbv_printer.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null);
                    wbv.zoomOut();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("onFailure", "MSG " + t.getMessage());
            }
        });


    }

    public void createPdf(View view){

        PrintManager printManager = (PrintManager) this.getSystemService(getBaseContext().PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                wbv_printer.createPrintDocumentAdapter("SIS");

        String jobName = getString(R.string.app_name) + "Absence_";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    class absence{
        public String etudeint;
        public String etat;

        public absence( String e, String et ){
            this.etudeint = e;
            this.etat = et;
        }

    }
}
