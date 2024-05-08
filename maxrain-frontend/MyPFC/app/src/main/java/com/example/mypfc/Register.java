package com.example.mypfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private Button btnRegister;
    private Spinner spinnerEleccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.button_register);
        spinnerEleccion = findViewById(R.id.spinner_eleccion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.flecha2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_documento, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEleccion.setAdapter(adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextTelefono = findViewById(R.id.sign_up_edittext_telefono);
                EditText editTextUsername = findViewById(R.id.sign_up_edittext_username);
                EditText editTextEmail = findViewById(R.id.sign_up_edittext_email);

                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String telefono = editTextEmail.getText().toString().trim();

                // Validación de los campos
                if (username.isEmpty()) {
                    editTextUsername.setError("El nombre de usuario es obligatorio");
                    editTextUsername.requestFocus();
                    return; // No procede al siguiente paso hasta que este campo no esté lleno
                }

                if (email.isEmpty()) {
                    editTextEmail.setError("El correo electrónico es obligatorio");
                    editTextEmail.requestFocus();
                    return; // No procede al siguiente paso hasta que este campo no esté lleno
                }

                if (username.isEmpty()) {
                    editTextUsername.setError("El nombre de usuario es obligatorio");
                    editTextUsername.requestFocus();
                    return;
                }

                if (!isValidEmail(email)) {
                    editTextEmail.setError("Introduce un correo electrónico válido");
                    editTextEmail.requestFocus();
                    return;
                }

                if (!isValidPhone(telefono)) {
                    editTextTelefono.setError("Introduce un número de teléfono válido");
                    editTextTelefono.requestFocus();
                    return;
                }

                Intent intent = new Intent(Register.this, RegisterSiguiente.class);
                startActivity(intent);
            }
        });


    }
    private boolean isValidEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email);
    }

    private boolean isValidPhone(String telefono) {
        return Pattern.matches("^\\+?\\d{10,15}$", telefono);
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