package com.example.sis;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.lang.Long.sum;

public class User implements Serializable {

    private String  id;
    private String  login;
    private String  password;
    private String  cin;
    private String  nom;
    private String  prenom;
    private String  email;
    private String  ville;
    private String  adresse;
    private String  codepostal;
    private String  cne;
    private String  role;
    private String  abs_etat;

    public User(){
        super();
    }
    public User(String d){
        super();
        fromString(d);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getCodepostal() { return codepostal; }
    public void setCodepostal(String codepostal) { this.codepostal = codepostal; }

    public String getCne() { return cne; }
    public void setCne(String cne) { this.cne = cne; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }


    public String getAbs_etat() {   return abs_etat; }
    public void setAbs_etat(String abs_etat) { this.abs_etat = abs_etat; }


    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
        //return id +"@__@"+ login +"@__@"+ password +"@__@"+ cin +"@__@"+ nom +"@__@"+ prenom +"@__@"+ email +"@__@"+ ville +"@__@"+ adresse +"@__@"+ codepostal +"@__@"+ cne;
    }

    public User fromString(String data){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<User>() {
        }.getType();
        Log.i("kjl",data);
        User u = gson.fromJson( data , collectionType);

        this.id = u.id;
        this.login = u.login;
        this.password = u.password;
        this.cin = u.cin;
        this.nom = u.nom;
        this.prenom = u.prenom;
        this.email = u.email;
        this.ville = u.ville;
        this.adresse = u.adresse;
        this.codepostal = u.codepostal;
        this.cne = u.cne;
        this.role = u.role;
        return this;
    }
}
