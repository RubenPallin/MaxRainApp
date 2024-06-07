package com.example.mypfc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    // Declaración de variables
    private Button btnRegister;
    public String email, nombre, rcontrasena, apellido, contrasena, telefono;
    public EditText editTextTelefono, editTextApellido, editTextEmail, editTextUsername, editTextContrasena, editTextrContrasena;

    private final Context context = Register.this;
    private RequestQueue queue;

    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Habilitar el modo EdgeToEdge
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        // Inicializar vistas y elementos del layout
        btnRegister = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // Configurar la barra de herramientas
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar ActionBar y flecha de retroceso
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            final Drawable upArrow = ContextCompat.getDrawable(this, com.google.android.material.R.drawable.abc_ic_ab_back_material);
            if (upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        // Inicializar la cola de solicitudes de Volley
        queue = Volley.newRequestQueue(context);

        // Asociar vistas con variables
        editTextTelefono = findViewById(R.id.sign_up_edittext_telefono);
        editTextApellido = findViewById(R.id.sign_up_edittext_apellidos);
        editTextUsername = findViewById(R.id.sign_up_edittext_username);
        editTextEmail = findViewById(R.id.sign_up_edittext_email);
        editTextContrasena = findViewById(R.id.sign_up_edittext_contrasena);
        editTextrContrasena = findViewById(R.id.sign_up_edittext_repetir_contrasena);

        // Configurar el listener del botón de registro
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener y validar datos
                nombre = editTextUsername.getText().toString().trim();
                apellido = editTextApellido.getText().toString().trim();
                email = editTextEmail.getText().toString().trim();
                telefono = editTextTelefono.getText().toString().trim();
                contrasena = editTextContrasena.getText().toString().trim();
                rcontrasena = editTextrContrasena.getText().toString().trim();

                if (validacionesDatos(editTextUsername, editTextEmail, editTextTelefono, editTextContrasena, editTextrContrasena)) {
                    // Enviar la solicitud de registro
                    sendPostRegisterRequest();
                }
            }
        });
    }

    // Método para validar el formato del correo electrónico
    private boolean isValidEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email);
    }

    // Método para validar el formato del número de teléfono
    private boolean isValidPhone(String telefono) {
        return Pattern.matches("^\\+?\\d{8,10}$", telefono);
    }

    // Método para enviar la solicitud de registro
    private void sendPostRegisterRequest() {
        // Crear el cuerpo de la solicitud
        JSONObject body = new JSONObject();
        try {
            body.put("nombre", nombre);
            body.put("apellido", apellido);
            body.put("email", email);
            body.put("telefono", telefono);
            body.put("contraseña", contrasena);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Crear la solicitud
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/registro/",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Si la respuesta es exitosa, enviar solicitud de inicio de sesión
                        sendLogRequest(email, contrasena);
                    }
                },
                new Response.ErrorListener() {
                    // Manejar errores en la solicitud
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(context, "No se pudo alcanzar al servidor", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                progressBar.setVisibility(View.INVISIBLE);
                                String data = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                JSONObject json_error_data = new JSONObject(data);
                                Toast.makeText(context, "Error: " + json_error_data.optString("error"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );
        // Agregar la solicitud a la cola
        queue.add(request);
    }

    // Método para enviar la solicitud de inicio de sesión
    private void sendLogRequest(String email, String contrasena) {
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("contraseña", contrasena);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/sesion/",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        String receivedToken;
                        try {
                            receivedToken = response.getString("token");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Guardar el token de sesión en SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MiSharedPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", receivedToken);
                        editor.apply();

                        // Navegar a la pantalla principal
                        Intent intent = new Intent(Register.this, MainMaxRain.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error.networkResponse == null) {
                            Toast.makeText(context, "No se pudo alcanzar al servidor en la segunda petición", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String data = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                JSONObject json_error_data = new JSONObject(data);
                                Toast.makeText(context, "Error: " + json_error_data.optString("error"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

        this.queue.add(request);
    }

    // Método para validar los datos de entrada
    private boolean validacionesDatos(EditText editTextUsername, EditText editTextEmail, EditText editTextTelefono, EditText editTextContraseña, EditText editTextrContraseña) {
        if (nombre.isEmpty()) {
            editTextUsername.setError("El nombre de usuario es obligatorio");
            editTextUsername.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("El correo electrónico es obligatorio");
            editTextEmail.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            editTextEmail.setError("Introduce un correo electrónico válido");
            editTextEmail.requestFocus();
            return false;
        }

        if (telefono.isEmpty()) {
            editTextTelefono.setError("El número de teléfono es obligatorio");
            editTextTelefono.requestFocus();
            return false;
        }

        if (!isValidPhone(telefono)) {
            editTextTelefono.setError("Introduce un número de teléfono válido");
            editTextTelefono.requestFocus();
            return false;
        }

        if (rcontrasena.equals("") || rcontrasena.equals("")) {
            Toast.makeText(context, "Introduce todos los datos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!contrasena.equals(rcontrasena)) {
            editTextContrasena.setText("");
            editTextrContraseña.setText("");
            Toast.makeText(context, "Las contraseñas han de ser iguales", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Manejar la selección de elementos del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
