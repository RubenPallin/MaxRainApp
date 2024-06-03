package com.example.mypfc;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.json.JSONException;

import java.util.List;
import android.content.SharedPreferences;

public class MiCuentaRegistrada extends AppCompatActivity {
    private TextView textCuentaRegistrada;
    private List<UsuariosData> usuariosDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mi_cuenta_regristrada); // Inicializar los elementos de la interfaz
        textCuentaRegistrada = findViewById(R.id.text_cuenta_registrada);
        Button buttonPedidos = findViewById(R.id.button_pedidos);
        Button buttonAlbaranes = findViewById(R.id.button_alabranes);
        Button buttonFacturas = findViewById(R.id.button_facturas);
        Button buttonFavoritos = findViewById(R.id.button_favoritos);
        Button btnSesion = findViewById(R.id.botonCerrarSesion);


        Toolbar toolbarRegis = findViewById(R.id.toolbar_perfil_registrado);
        setSupportActionBar(toolbarRegis);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Cambiar el color de la flecha de retroceso
            final Drawable upArrow = ContextCompat.getDrawable(this, com.google.android.material.R.drawable.abc_ic_ab_back_material);
            if (upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        // Configurar onClick para los botones
        buttonPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón de Pedidos
            }
        });

        buttonAlbaranes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón de Albaranes
            }
        });

        buttonFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón de Facturas
            }
        });

        buttonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el botón de Favoritos
            }
        });

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             cerrarSesion(v);
            }
        });

        // Obtener SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Recuperar los datos del usuario
        String nombre = sharedPreferences.getString("nombre", null);
        String apellido = sharedPreferences.getString("apellido", null);
        String email = sharedPreferences.getString("email", null);
        String telefono = sharedPreferences.getString("telefono", null);

        Log.d("MiCuentaRegistrada", "Nombre: " + nombre);
        Log.d("MiCuentaRegistrada", "Apellido: " + apellido);
        Log.d("MiCuentaRegistrada", "Email: " + email);
        Log.d("MiCuentaRegistrada", "Telefono: " + telefono);

        // Asegurarse de que los datos no sean nulos antes de usarlos
        if (nombre != null && apellido != null) {
            UsuariosData usuario = new UsuariosData(nombre, apellido, null, email, telefono);
            textCuentaRegistrada.setText(usuario.getNombre() + " " + usuario.getApellido());
        } else {
            textCuentaRegistrada.setText("Usuario desconocido");
        }
    }
    public void cerrarSesion(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirigir a la pantalla de inicio de sesión o cerrar la actividad actual
        Intent intent = new Intent(this, MiCuenta.class);
        startActivity(intent);
        finish(); // Opcional: cierra la actividad actual
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Manejar el clic en el botón de retroceso
            Intent intent = new Intent(this, MainMaxRain.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}