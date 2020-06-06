package com.happynacho.citashc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuSelection extends AppCompatActivity {
    private Button btnAgendar,btnCitas,btnPosponer;
    private String patient_id,qr;
    private boolean getQR;

    private CitaDBMS citasDB;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_selection);
        this.citasDB = new CitaDBMS(this);
        this.getQR=false;
        patient_id = getIntent().getStringExtra("patient_id");
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        qr = getIntent().getStringExtra("QR");
        if(qr==null) fillDB();
        else if(qr.length()>2 && qr.substring(0,2).equals("QR")){
            String[] patientQR = qr.split("/");
            if(patientQR.length==6) {
                this.getQR=true;
                /*edtPatient.setText(patientQR[1]);
                edtName.setText(patientQR[2]);
                edtAge.setText(patientQR[3]);
                edtGender.setText(patientQR[4]);
                edtAllergic.setText(patientQR[5]);*/

            }
            else {
                Toast.makeText(this,"QR Invalido",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"QR Invalido",Toast.LENGTH_SHORT).show();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId()){
                case R.id.nav_agendar:
                    selectedFragment = new AgendarFragment();
                    break;
                case R.id.nav_ver_citas:
                    selectedFragment = new CitasFragment();
                    break;
                case R.id.nav_modificar:
                    selectedFragment = new ModificarFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

    public CitaDBMS getDB(){
        return this.citasDB;
    }
    public String getPatientID(){
        return this.patient_id;
    }
    public String getQRString(){
        return this.qr;
    }
    public boolean hasQR(){
        return this.getQR;
    }
    private void fillDB(){
        RequestQueue queue = Volley.newRequestQueue(this);

        final String url ="http://192.168.100.6:8080/hcg/buscar_citas.php?patient_id="+this.patient_id;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(null,"Response is: "+ response);
                        JSONArray jsonArray = null;
                        JSONObject jsonObject = null;
                        try {
                            jsonArray = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0; i < jsonArray.length();i++){
                            try {
                                jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("patient_id");
                                String date = jsonObject.getString("date");
                                String description = jsonObject.getString("description");
                                Log.e(null,"Response222is: "+id+date+description);
                                Cita cita = new Cita(id,date,description);
                                citasDB.insertCita(cita);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(null,"That didn't work!");

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
