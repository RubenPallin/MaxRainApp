package com.example.mypfc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.mypfc.databinding.ActivityMainMaxRainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;



public class MainMaxRain extends AppCompatActivity {
    ActivityMainMaxRainBinding binding;
    private  Context main_context = this;
    private TextView textViewResult;
    private ImageButton btnQr;
    private int lastSelectedItemId = R.id.nav_item1;
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
        BottomNavigationView bar = findViewById(R.id.bottomNavigation);


        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_item1) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new InicioFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (itemId == R.id.nav_item2) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new FamiliasFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (itemId == R.id.nav_item3) {
                    Intent intent = new Intent(MainMaxRain.this, Carrito.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_item4) {
                    // Manejar cuarto ítem
                    startActivity(new Intent(MainMaxRain.this, MiCuenta.class));
                }
                return true;
            }
        });

        if (savedInstanceState != null) {
            // Restaurar el último ítem seleccionado si es necesario
            lastSelectedItemId = savedInstanceState.getInt("LAST_SELECTED_ITEM_ID", R.id.nav_item1);
        }
        bar.setSelectedItemId(lastSelectedItemId); // Establecer el ítem seleccionado al iniciar


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
        options.setPrompt("ESCANY el main:EAR CODIGO");

        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        options.setCaptureActivity(ActivityEscanear.class);
        options.setBarcodeImageEnabled(false);

        barcodeLauncher.launch(options);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView bar = findViewById(R.id.bottomNavigation);
        if (bar.getSelectedItemId() != lastSelectedItemId) {
            bar.setSelectedItemId(lastSelectedItemId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("LAST_SELECTED_ITEM_ID", lastSelectedItemId); // Guardar el último ítem seleccionado
    }
}