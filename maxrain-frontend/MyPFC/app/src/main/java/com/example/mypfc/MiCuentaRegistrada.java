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
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MiCuentaRegistrada extends AppCompatActivity {
    private TextView textCuentaRegistrada;
    private List<UsuariosData> usuariosDataList;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    static final String KEY_TOKEN = "token";
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
        Button btnAjustes = findViewById(R.id.boton_ajustes2);


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

        btnAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiCuentaRegistrada.this, AjustesTema.class);
                startActivity(intent);
            }
        });

        obtenerNombreyApellido();

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

    private void obtenerNombreyApellido() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String sessionToken = sharedPreferences.getString(KEY_TOKEN, "");

        if (sessionToken == null || sessionToken.isEmpty()) {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url= "http://10.0.2.2:8000/usuario/";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nombre = response.getString("nombre");
                            String apellido = response.getString("apellido");
                            mostrarNombreApellido(nombre, apellido);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(MiCuentaRegistrada.this, "No se ha establecido la conexión", Toast.LENGTH_SHORT).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(MiCuentaRegistrada.this, "Estado del servidor: " + serverCode, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", sessionToken);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void mostrarNombreApellido(String nombre, String apellido) {
        String nombreCompleto = nombre + " " + apellido;
        textCuentaRegistrada.setText(nombreCompleto);
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