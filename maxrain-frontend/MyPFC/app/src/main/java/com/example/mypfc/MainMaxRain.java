package com.example.mypfc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypfc.databinding.ActivityMainMaxRainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;



public class MainMaxRain extends AppCompatActivity {
    ActivityMainMaxRainBinding binding;
    private  Context main_context = this;
    private TextView textViewResult;
    private ImageButton btnQr;
    private static final int CODIGO_PETICION_CAMARA = 1;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "CANCELADO", Toast.LENGTH_SHORT).show();
        } else {
            textViewResult.setText(result.getContents());
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_max_rain);
        btnQr = findViewById(R.id.imageButton);
        binding = ActivityMainMaxRainBinding.inflate(getLayoutInflater());
        textViewResult = findViewById(R.id.textViewResult);
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
                escanear();
            }

        });

    }

    public void escanear() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        options.setPrompt("ESCANEAR CODIGO");
        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        options.setCaptureActivity(ActivityEscanear.class);
        options.setBarcodeImageEnabled(false);

        barcodeLauncher.launch(options);
    }
}