package com.happynacho.citashc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class AgendarFragment extends Fragment {
    Button btnScan,btnAgendar;
    EditText edtPatient,edtName,edtAge,edtGender,edtAllergic,edtDescription;
    RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_agendar, container, false);
        edtPatient = vista.findViewById(R.id.edtPatientID);
        edtPatient.setEnabled(false);
        edtName = vista.findViewById(R.id.edtName);
        edtName.setEnabled(false);
        edtAge = vista.findViewById(R.id.edtAge);
        edtAge.setEnabled(false);
        edtGender = vista.findViewById(R.id.edtGender);
        edtGender.setEnabled(false);
        edtAllergic = vista.findViewById(R.id.edtAllergic);
        edtAllergic.setEnabled(false);

        edtDescription = vista.findViewById(R.id.edtDescription);

        btnScan = vista.findViewById(R.id.btnScan);
        btnAgendar = vista.findViewById(R.id.btnAgendar);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),QRScanner.class));
            }
        });
        btnAgendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("http://192.168.100.6:8080/hcg/insertar_cita.php");

            }
        });

        if(((MenuSelection)getActivity()).hasQR()) {
            String qr = ((MenuSelection) getActivity()).getQRString();
            String[] patientQR = qr.split("/");
            if (patientQR.length == 6) {
                edtPatient.setText(patientQR[1]);
                edtName.setText(patientQR[2]);
                edtAge.setText(patientQR[3]);
                edtGender.setText(patientQR[4]);
                edtAllergic.setText(patientQR[5]);
            }
        }
        return vista;
    }
    private void ejecutarServicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),"OPERACION EXITOSA",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
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
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
