package com.example.sis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar ;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pref;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        user = new User(pref.getString("UserLogin", null ) );

        /*Log.i("kjl2",user.toString());

        ActionBar actionBar = this.getSupportActionBar();
        String classTitle = actionBar.getTitle().toString();

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar);
        View view = actionBar.getCustomView();
        TextView activity_title = view.findViewById(R.id.activity_title);
        activity_title.setText(classTitle);
        TextView user_id_name = view.findViewById(R.id.user_id_name);
        user_id_name.setText( user.getNom() + "  |  " + user.getPrenom());

        view.findViewById(R.id.to_home).setBackgroundResource(R.drawable.ic_logout);*/
    }
    public void profile(View view){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void consultabsences(View view){
        Intent intent = new Intent(this, ConsultAbsence.class);
        startActivity(intent);
    }

    public void ficheappel(View view){
        Intent intent = new Intent(this, FicheAppel.class);
        startActivity(intent);
    }

    public void ajouteretudient(View view){
        Intent intent = new Intent(this, AjouterEtudiant.class);
        startActivity(intent);
    }

    public void logout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Êtes-vous sûr ?")
                .setIcon(R.drawable.icon_back)
                .setTitle(R.string.logout)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        logout_fn();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .show();
    }
    public void logout_fn(){
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

        finish();
    }
    public void onOptionsItemSelected(View item){
    }
}
