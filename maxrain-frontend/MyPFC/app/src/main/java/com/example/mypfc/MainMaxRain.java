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
import androidx.recyclerview.widget.RecyclerView;

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
        textViewResult = findViewById(R.id.textViewResult);
        BottomNavigationView bar = findViewById(R.id.bottomNavigation);


        bar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_item1) {

                } else if (itemId == R.id.nav_item2) {
                    // Carga el ProductosFragment
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new ProductosFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (itemId == R.id.nav_item3) {
                    // Manejar tercer ítem, por ejemplo abrir otra actividad
                    Intent intent = new Intent(MainMaxRain.this, Carrito.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_item4) {
                    // Manejar cuarto ítem
                    startActivity(new Intent(MainMaxRain.this, MiCuenta.class));
                } else {
                // Guardar el último ítem seleccionado que no es el carrito
                lastSelectedItemId = itemId;
                handleNavigation(itemId);
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
        options.setPrompt("ESCANEAR CODIGO");
        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(false);
        options.setCaptureActivity(ActivityEscanear.class);
        options.setBarcodeImageEnabled(false);

        barcodeLauncher.launch(options);
    }

    private void handleNavigation(int itemId) {
        if (itemId == R.id.nav_item1) {
            startActivity(new Intent(MainMaxRain.this, MainMaxRain.class));
        } else if (itemId == R.id.nav_item2) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProductosFragment())
                    .addToBackStack(null)
                    .commit();
        }
        // Añadir más casos usando 'else if' si es necesario
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restablecer el último ítem seleccionado cuando la actividad se reanuda
        BottomNavigationView bar = findViewById(R.id.bottomNavigation);
        bar.setSelectedItemId(lastSelectedItemId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("LAST_SELECTED_ITEM_ID", lastSelectedItemId); // Guardar el último ítem seleccionado
    }
}