package com.example.mypfc;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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

public class Login extends AppCompatActivity {
    // Declaración de variables
    private EditText editTextEmail, editTextContraseña;
    //private FirebaseAuth mAuth;
    private TextView textView;
    private ProgressBar progressBar;
    private RequestQueue queueForRequests;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Habilitar el modo EdgeToEdge
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Inicializar la cola de solicitudes de Volley
        queueForRequests = Volley.newRequestQueue(this);
        // Asociar vistas con variables
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        textView = findViewById(R.id.textEnlace);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Button btnLogin = findViewById(R.id.sign_in_login_button);

        // Configurar el listener del botón de inicio de sesión
        btnLogin.setOnClickListener(v -> iniciarSesion());

        // Mostrar el botón de retroceso
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        // Subrayar y configurar el texto clicable para registrar
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la pantalla de registro
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        // Configurar el listener del botón de inicio de sesión (otra vez para asegurar el comportamiento deseado)
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                iniciarSesion();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar la selección de elementos del menú
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Método para iniciar sesión
    private void iniciarSesion(){
        // Crear el cuerpo de la solicitud con los datos de entrada del usuario
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", editTextEmail.getText().toString());
            requestBody.put("contraseña", editTextContraseña.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Crear la solicitud de inicio de sesión
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/sesion/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Ocultar el ProgressBar
                        progressBar.setVisibility(View.INVISIBLE);
                        String receivedToken;
                        try {
                            // Obtener el token de la respuesta
                            receivedToken = response.getString("token");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Guardar el token y marcar al usuario como logueado
                        guardarToken(receivedToken);

                        // Navegar a la pantalla principal
                        Intent intent = new Intent(Login.this, MainMaxRain.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Ocultar el ProgressBar y manejar errores
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Toast.makeText(Login.this, "No se ha encontrado ese usuario", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Login.this, "Error de conexión", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        // Agregar la solicitud a la cola
        queueForRequests.add(request);
    }

    // Método para guardar el token de autenticación y marcar al usuario como logueado
    private void guardarToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
}
