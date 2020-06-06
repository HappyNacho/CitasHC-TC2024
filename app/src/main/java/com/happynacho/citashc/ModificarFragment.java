package com.happynacho.citashc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;


public class ModificarFragment extends Fragment {
    Button btnEliminar,btnPosponer;
    EditText edtPatient,edtName,edtAge,edtGender,edtAllergic,edtDescription,edtDate;
    RequestQueue requestQueue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_modificar, container, false);

        String url ="http://192.168.100.6:8080/hcg/ultima_cita.php?patient_id="+((MenuSelection)getActivity()).getPatientID();

        edtPatient = vista.findViewById(R.id.edtPatientIDM);
        edtPatient.setEnabled(false);
        edtName = vista.findViewById(R.id.edtNameM);
        edtName.setEnabled(false);
        edtAge = vista.findViewById(R.id.edtAgeM);
        edtAge.setEnabled(false);
        edtGender = vista.findViewById(R.id.edtGenderM);
        edtGender.setEnabled(false);
        edtAllergic = vista.findViewById(R.id.edtAllergicM);
        edtAllergic.setEnabled(false);

        edtDescription = vista.findViewById(R.id.edtDescriptionM);
        edtDescription.setEnabled(false);

        edtDate = vista.findViewById(R.id.edtDateM);
        edtDate.setEnabled(false);

        btnEliminar = vista.findViewById(R.id.btnEliminar);
        btnPosponer = vista.findViewById(R.id.btnPosponer);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCita("http://192.168.100.6:8080/hcg/eliminar_cita.php");
            }
        });

        btnPosponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("http://192.168.100.6:8080/hcg/editar_cita.php");
            }
        });



        llenardatos(url);
        return vista;
    }

    private void llenardatos(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtPatient.setText(jsonObject.getString("patient_id"));
                        edtName.setText(jsonObject.getString("patient_name"));
                        edtAge.setText(jsonObject.getString("age"));
                        edtGender.setText(jsonObject.getString("gender"));
                        edtAllergic.setText(jsonObject.getString("allergic_medication"));
                        edtDescription.setText(jsonObject.getString("description"));
                        edtDate.setText(jsonObject.getString("date"));


                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"ERROR DE CONEXION",Toast.LENGTH_SHORT);
            }
        }
        );
        requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
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
                parametros.put("date",edtDate.getText().toString());
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
                parametros.put("newdate",citaDate);
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void eliminarCita(String URL){
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
                parametros.put("date",edtDate.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
