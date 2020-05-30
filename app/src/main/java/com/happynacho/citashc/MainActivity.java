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
import android.widget.ImageView;

import com.happynacho.citashc.Map.NearestHostpital;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button buttonSOS;
    Button buttonLogin;
    ImageView imageViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }



        buttonSOS = findViewById(R.id.buttonSOS);
        buttonSOS.setOnClickListener( this);

        buttonLogin=findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        imageViewMap= findViewById(R.id.imageViewMap);
        imageViewMap.setOnClickListener(this);
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

        if( v.getId() == R.id.buttonLogin){

        }

        if( v.getId() == R.id.imageViewMap){

            setContentView(R.layout.activity_nearest_hostpital);

        }

    }
}