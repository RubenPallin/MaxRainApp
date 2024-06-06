package com.example.mypfc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AjustesTema extends AppCompatActivity {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String PREF_DARK_MODE = "pref_dark_mode";
    private static final String KEY_CART_ITEMS = "CartItems";
    static final String KEY_TOKEN = "token";
    private RequestQueue queue;
    private View eliminar_cuenta;
    private Switch modo_oscuro;
    private View progressBar; // Assumes progressBar is defined somewhere in the layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_tema);

        Toolbar toolbarArt = findViewById(R.id.toolbar_ajustes);
        setSupportActionBar(toolbarArt);

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


        queue = Volley.newRequestQueue(this);

        // Asume que delete_account y progressBar están definidos en el layout correspondiente
        eliminar_cuenta = findViewById(R.id.eliminar_cuenta_boton);
        modo_oscuro = findViewById(R.id.modo_oscuro);


        // Configurar el listener para el botón de eliminar cuenta
        delete_account_func();
        cambiar_modo_oscuro();
    }

    private void delete_account_func() {
        eliminar_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AjustesTema.this);
                builder.setMessage("¿Estás seguro de que deseas eliminar tu cuenta?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_account_request();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void delete_account_request() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String sessionToken = sharedPreferences.getString(KEY_TOKEN, "");

        // Crear la solicitud JSON para eliminar la cuenta
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                "http://10.0.2.2:8000/eliminar_sesion/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Eliminar los datos de SharedPreferences y regresar a la pantalla de login
                        SharedPreferences.Editor editor = getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE).edit();
                        editor.clear().apply();
                        startActivity(new Intent(AjustesTema.this, Login.class));
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Ocultar el ProgressBar
                        progressBar.setVisibility(View.INVISIBLE);

                        if (error.networkResponse == null) {
                            Toast.makeText(AjustesTema.this, "No se puede conectar al servidor", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                // Obtener la respuesta del servidor
                                String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                JSONObject errorJson = new JSONObject(responseBody);
                                String errorMessage = errorJson.getString("error");

                                // Mostrar el mensaje de error al usuario
                                Toast.makeText(AjustesTema.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", sessionToken);
                return headers;
            }
        };

        // Añadir la solicitud a la cola
        queue.add(jsonObjectRequest);
    }

    private void cambiar_modo_oscuro() {
        // Cargar el estado del modo oscuro desde las preferencias compartidas
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = preferences.getBoolean(PREF_DARK_MODE, false);

        // Establecer el estado del interruptor basado en el modo oscuro actual
        modo_oscuro.setChecked(isDarkMode);

        // Manejar el cambio del interruptor
        modo_oscuro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Guardar el estado del modo oscuro en las preferencias compartidas
                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(PREF_DARK_MODE, isChecked);
                editor.apply();

                // Cambiar dinámicamente el tema de la aplicación
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                // Recrear la actividad para aplicar el cambio de tema inmediatamente
                recreate();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}