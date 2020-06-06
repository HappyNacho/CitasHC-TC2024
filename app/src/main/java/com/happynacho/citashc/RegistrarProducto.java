package com.happynacho.citashc;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarProducto extends AppCompatActivity {

    EditText edtCodigo,edtProducto,edtPrecio,edtFabricante;
    Button btnAgregar,btnBuscar,btnEditar,btnEliminar;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);

        edtCodigo = (EditText) findViewById(R.id.edtCodigo);
        edtProducto = (EditText) findViewById(R.id.edtProducto);
        edtPrecio = (EditText) findViewById(R.id.edtPrecio);
        edtFabricante = (EditText) findViewById(R.id.edtFabricante);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("https://happynacho.com/insertar_producto.php");
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarProducto("https://happynacho.com/buscar_producto.php?coding="+edtCodigo.getText().toString()+"");
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejecutarServicio("https://happynacho.com/editar_producto.php");
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto("https://happynacho.com/eliminar_producto.php");
            }
        });
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
                parametros.put("codigo",edtCodigo.getText().toString());
                parametros.put("producto",edtProducto.getText().toString());
                parametros.put("precio",edtPrecio.getText().toString());
                parametros.put("fabricante",edtFabricante.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void buscarProducto(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtProducto.setText(jsonObject.getString("producto"));
                        edtPrecio.setText(jsonObject.getString("precio"));
                        edtFabricante.setText(jsonObject.getString("fabricante"));
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
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void eliminarProducto(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"El producto fue eliminado",Toast.LENGTH_SHORT).show();
                limpiarFormulario();
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
                parametros.put("codigo",edtCodigo.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void limpiarFormulario(){
        edtCodigo.setText("");
        edtProducto.setText("");
        edtPrecio.setText("");
        edtFabricante.setText("");
    }
}

