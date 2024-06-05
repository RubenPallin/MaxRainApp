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
        setContentView(R.layout.activity_mi_cuenta_regristrada);
        textCuentaRegistrada = findViewById(R.id.text_cuenta_registrada);
        Button btnPedidos = findViewById(R.id.boton_pedidos);
        Button btnAlbaranes = findViewById(R.id.boton_alabranes);
        Button btnFacturas = findViewById(R.id.boton_facturas);
        Button btnFavoritos = findViewById(R.id.boton_favoritos);
        Button btnSesion = findViewById(R.id.botonCerrarSesion);
        Button btnDatos = findViewById(R.id.boton_datos);
        Button btnContacto = findViewById(R.id.boton_contacto2);


        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Recuperar los datos del usuario
        String nombre = sharedPreferences.getString("nombre", null);
        String apellido = sharedPreferences.getString("apellido", null);
        String email = sharedPreferences.getString("email", null);
        String telefono = sharedPreferences.getString("telefono", null);

        // Asegurarse de que los datos no sean nulos antes de usarlos
        if (nombre != null && apellido != null) {
            // Crear un objeto UsuariosData si los datos no son nulos
            UsuariosData usuario = new UsuariosData(nombre, apellido, null, email, telefono);
            textCuentaRegistrada.setText(usuario.getNombre() + " " + usuario.getApellido());
        } else {
            // Si alguno de los datos es nulo, mostrar un mensaje de usuario desconocido
            textCuentaRegistrada.setText("Usuario desconocido");
        }


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
        btnPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiCuentaRegistrada.this, Pedidos.class);
                startActivity(intent);
            }
        });

        btnAlbaranes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiCuentaRegistrada.this, Albaranes.class);
                startActivity(intent);
            }
        });

        btnFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiCuentaRegistrada.this, Facturas.class);
                startActivity(intent);
            }
        });

        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiCuentaRegistrada.this, Favoritos.class);
                startActivity(intent);
            }
        });

        btnContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiCuentaRegistrada.this, Contacto.class);
                startActivity(intent);
            }
        });

        btnDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiCuentaRegistrada.this, MisDatos.class);
                startActivity(intent);
            }
        });

        btnSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             cerrarSesion(v);
            }
        });

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