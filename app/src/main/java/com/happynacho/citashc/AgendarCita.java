package com.happynacho.citashc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AgendarCita extends AppCompatActivity {
    Button btnScan,btnAgendar;
    EditText edtPatient,edtName,edtAge,edtGender,edtAllergic,edtDescription;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        edtPatient = findViewById(R.id.edtPatientID);
        edtPatient.setEnabled(false);
        edtName = findViewById(R.id.edtName);
        edtName.setEnabled(false);
        edtAge = findViewById(R.id.edtAge);
        edtAge.setEnabled(false);
        edtGender = findViewById(R.id.edtGender);
        edtGender.setEnabled(false);
        edtAllergic = findViewById(R.id.edtAllergic);
        edtAllergic.setEnabled(false);

        edtDescription = findViewById(R.id.edtDescription);

        btnScan = findViewById(R.id.btnScan);
        btnAgendar = findViewById(R.id.btnAgendar);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QRScanner.class));
            }
        });
        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("https://happynacho.com/insertar_producto.php");
            }
        });
        String qr = getIntent().getStringExtra("QR");
        if(qr==null){

        }
        else if(qr.length()>2 && qr.substring(0,2).equals("QR")){
            String[] patientQR = qr.split("/");
            if(patientQR.length==6) {
                edtPatient.setText(patientQR[1]);
                edtName.setText(patientQR[2]);
                edtAge.setText(patientQR[3]);
                edtGender.setText(patientQR[4]);
                edtAllergic.setText(patientQR[5]);


            }
            else {
                Toast.makeText(this,"QR Invalido",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"QR Invalido",Toast.LENGTH_SHORT).show();
        }


    }
    private void ejecutarServicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"OPERACION EXITOSA",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("patient_id",edtPatient.getText().toString());
                parametros.put("description",edtDescription.getText().toString());
                String citaDate = "";
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 7);
                if(calendar.DAY_OF_WEEK == Calendar.SATURDAY || calendar.DAY_OF_WEEK == Calendar.SUNDAY){
                    calendar.add(Calendar.DAY_OF_YEAR, 2);
                    SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = sdf.format(calendar.getTime());
                    int randomHours = ThreadLocalRandom.current().nextInt(15, 18 + 1);
                    int randomMinutes = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                    citaDate = currentTime.substring(0,10)+" "+randomHours+":"+randomMinutes*15+":"+"00";
                }
                parametros.put("date",citaDate);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
