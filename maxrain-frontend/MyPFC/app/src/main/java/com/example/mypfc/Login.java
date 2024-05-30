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
    private EditText editTextEmail, editTextContraseña;
    //private FirebaseAuth mAuth;
    private TextView textView;
    private ProgressBar progressBar;
    private RequestQueue queueForRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        queueForRequests = Volley.newRequestQueue(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContraseña = findViewById(R.id.editTextContraseña);
        textView = findViewById(R.id.textEnlace);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Button btnLogin = findViewById(R.id.sign_in_login_button);

        btnLogin.setOnClickListener(v -> iniciarSesion());

        // Show the button to go back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniciarSesion(){

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("email", editTextEmail.getText().toString());
            requestBody.put("contraseña", editTextContraseña.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8000/sesion/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String receivedToken;
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            receivedToken = response.getString("token");
                            Log.d("SavedToken", receivedToken);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        // Obtén una referencia a SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        // Crea un editor de SharedPreferences para realizar cambios
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // Almacena el token de sesión
                        editor.putString("token", receivedToken);
                        // Guarda los cambios
                        editor.apply();
                        Log.d("SavedToken", receivedToken);

                        Intent intent = new Intent(Login.this, MainMaxRain.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

        queueForRequests.add(request);
    }
}