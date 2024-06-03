package com.example.mypfc;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;



public class MainMaxRain extends AppCompatActivity {
    com.example.mypfc.databinding.ActivityMainMaxRainBinding binding;
    private  Context main_context = this;
    private TextView textViewResult;
    private ImageButton btnQr;
    private int lastSelectedItemId = R.id.nav_item1;
    private static final int CODIGO_PETICION_CAMARA = 1;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

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
        binding = binding;
        BottomNavigationView bar = findViewById(R.id.bottomNavigation);
        // Verificar si el usuario está registrado
        boolean loggedIn = isLoggedIn();


        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Ajustar la interfaz o mostrar un mensaje basado en el estado de loggedIn
        if (loggedIn) {
            Toast.makeText(this, "Usuario está registrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario no está registrado", Toast.LENGTH_SHORT).show();
        }
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
                    if (isLoggedIn()) {
                        // El usuario está registrado, iniciar la actividad MiCuentaRegistrada
                        Intent intent = new Intent(MainMaxRain.this, CarritoRegistrado.class);
                        startActivity(intent);
                        finish(); // Opcional: Finaliza esta actividad para evitar que el usuario regrese aquí al presionar el botón Atrás
                    } else {
                        // El usuario no está registrado, redirigirlo a la actividad de inicio de sesión (Login)
                        Intent intent = new Intent(MainMaxRain.this, Carrito.class);
                        startActivity(intent);
                        finish(); // Opcional: Finaliza esta actividad para evitar que el usuario regrese aquí al presionar el botón Atrás
                    }
                } else if (itemId == R.id.nav_item4) {
                    if (isLoggedIn()) {
                        // El usuario está registrado, iniciar la actividad MiCuentaRegistrada
                        Intent intent = new Intent(MainMaxRain.this, MiCuentaRegistrada.class);
                        startActivity(intent);
                        finish(); // Opcional: Finaliza esta actividad para evitar que el usuario regrese aquí al presionar el botón Atrás
                    } else {
                        // El usuario no está registrado, redirigirlo a la actividad de inicio de sesión (Login)
                        Intent intent = new Intent(MainMaxRain.this, MiCuenta.class);
                        startActivity(intent);
                        finish(); // Opcional: Finaliza esta actividad para evitar que el usuario regrese aquí al presionar el botón Atrás
                    }
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

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.contains(KEY_TOKEN);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void cerrarSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN); // Eliminar el token de autenticación
        editor.remove(KEY_IS_LOGGED_IN); // Eliminar la marca de inicio de sesión
        editor.apply();
    }
}