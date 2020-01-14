package com.example.sis;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIEndPoint {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(@Header("accept") String type, @Field("login") String Username, @Field("password") String Password);

    @FormUrlEncoded
    @POST("ajouter_etudient.php")
    Call<ResponseBody> ajouter_etudient(
            @Header("accept") String type,
            @Field("cne") String cne,
            @Field("password") String password,
            @Field("cin") String cin,
            @Field("nom") String nom,
            @Field("prenom") String prenom,
            @Field("email") String email,
            @Field("ville") String ville,
            @Field("adresse") String adresse,
            @Field("codepostal") String codepostal
    );

    @FormUrlEncoded
    @POST("liste_etudiants.php")
    Call<ResponseBody> liste_etudiants(
            @Header("accept") String type,
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("submit_absence.php")
    Call<ResponseBody> submit_absence(
            @Header("accept") String type,
            @Field("data") String data
    );


    @FormUrlEncoded
    @POST("get_absences.php")
    Call<ResponseBody> get_absences(
            @Header("accept") String type,
            @Field("date") String date
    );




    @FormUrlEncoded
    @POST("Grades.php")
    Call<ResponseBody> Grades(
            @Header("accept") String type,
            @Field("id") String StdID,
            @Field("year") String year,
            @Field("semester") String semester
    );


    @FormUrlEncoded
    @POST("Editeprofile.php")
    Call<ResponseBody> Editeprofile(
            @Header("accept") String type,
            @Field("id") String StdID,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("adresse") String adresse
    );

    @FormUrlEncoded
    @POST("Plan.php")
    Call<ResponseBody> Plan(
            @Header("accept") String type,
            @Field("id") String StdID
    );

    @FormUrlEncoded
    @POST("Plan_details.php")
    Call<ResponseBody> Plan_details(
            @Header("accept") String type,
            @Field("id") String StdID,
            @Field("school") String school,
            @Field("type") String label
    );

    @FormUrlEncoded
    @POST("Timetable.php")
    Call<ResponseBody> Timetable(
            @Header("accept") String type,
            @Field("id") String StdID,
            @Field("center") String center,
            @Field("code") String code
    );

    @FormUrlEncoded
    @POST("Registration.php")
    Call<ResponseBody> Registration(
            @Header("accept") String type,
            @Field("id") String StdID,
            @Field("cours_id") int program_id,
            @Field("year") String year,
            @Field("semester") String semester,
            @Field("filiere_id") int filiere_id,
            @Field("role") String role
            //,@Field("ids") String ids
            );

    @FormUrlEncoded
    @POST("Results.php")
    Call<ResponseBody> Results(@Header("accept") String type, @Field("id") String StdID, @Field("year") String Year);


    @FormUrlEncoded
    @POST("Fees.php")
    Call<ResponseBody> Fees(@Header("accept") String type, @Field("id") String StdID);


}