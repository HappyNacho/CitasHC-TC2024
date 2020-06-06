package com.happynacho.citashc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edtUser, edtPassword;
    Button buttonSOS;
    Button buttonLogin;
    ImageView imageViewMap;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        edtUser = findViewById(R.id.editTextUser);
        edtPassword = findViewById(R.id.editTextPassword);

        buttonSOS = findViewById(R.id.buttonSOS);
        buttonSOS.setOnClickListener( this);

        buttonLogin=findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login("http://192.168.100.6:8080/hcg/login.php?patient_id="+edtUser.getText().toString());
            }
        });

        imageViewMap= findViewById(R.id.imageViewMap);
        imageViewMap.setOnClickListener(this);


    }

    private void login(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String id = jsonObject.getString("patient_id");
                        String password = jsonObject.getString("password");
                        if(edtUser.getText().toString().equals(id)&&MD5.getMd5(edtPassword.getText().toString()).equals(password)){
                            Intent intent = new Intent(getApplicationContext(),MenuSelection.class);
                            intent.putExtra("patient_id",id);
                            startActivity(intent);

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"ERROR DE CONEXION",Toast.LENGTH_SHORT);
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {


        if( v.getId() == R.id.buttonSOS){

            Intent intent = new Intent(Intent.ACTION_CALL);

            int permissionCheck = ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 225);
            } else {

                intent.setData(Uri.parse("tel:911"));
                startActivity(intent);

            }
        }



        if( v.getId() == R.id.imageViewMap){

            startActivity(new Intent(this,MapsActivity.class));

        }

    }
}