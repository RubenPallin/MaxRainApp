package com.example.mypfc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainMaxRain extends AppCompatActivity {
    private  Context main_context = this;
    private ImageButton btnQr;
    private static final int CODIGO_PETICION_CAMARA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_max_rain);
        btnQr = findViewById(R.id.imageButton);

        BottomNavigationView bar = findViewById(R.id.bottom_navigation);


        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_item1){

                } else if (menuItem.getItemId() == R.id.nav_item2) {
                    Intent intent = new Intent(main_context, Login.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.nav_item3) {
                    Intent intent = new Intent(main_context, CarritoFragment.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.nav_item4) {
                    Intent intent = new Intent(main_context, MiCuenta.class);
                    startActivity(intent);
                }
                return false;
            }
        }

        );

        btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCamaraParaQR();
            }

            private void abrirCamaraParaQR() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CODIGO_PETICION_CAMARA);
            }


        });



    }
}