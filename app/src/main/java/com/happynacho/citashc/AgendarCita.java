package com.happynacho.citashc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AgendarCita extends AppCompatActivity {
    Button btnScan,btnAgendar;
    EditText edtPatient,edtName,edtAge,edtGender,edtAllergic;

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

        btnScan = findViewById(R.id.btnScan);
        btnAgendar = findViewById(R.id.btnAgendar);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),QRScanner.class));
            }
        });

        if(getIntent().getStringExtra("QR")==null){

        }
        else if(getIntent().getStringExtra("QR").substring(0,2).equals("QR")){
            String qr = getIntent().getStringExtra("QR");
            String[] patientQR = qr.split("/");
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
}
