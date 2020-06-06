package com.happynacho.citashc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class ShowCitas extends AppCompatActivity {
    ArrayList<Cita> historial;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    AdapterCitas adapter;


    RecyclerView recycler;
    RequestQueue requestQueue;
    private String patient_id;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        this.patient_id = getIntent().getStringExtra("patient_id");
        this.url="http://192.168.100.6:8080/hcg/buscar_citas.php?patient_id="+this.patient_id;





        recycler = findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        adapter = new AdapterCitas(this,historial);
        recycler.setAdapter(adapter);



    }




}