package com.example.mypfc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        Button btnLogin = findViewById(R.id.sign_in_login_button);

        btnLogin.setOnClickListener(v -> iniciarSesion());

        // Show the button to go back
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

    }

    private void iniciarSesion(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

/*        mAuth.signInWithEmailandPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()) {
//                       startActivity(new Intent(Login.this, MiCuentaRegistrada.));
                         finish();
                    } else {
                        Toast.makeText(Login.this, "Error al iniciar sesión: "
                         + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }); */

    }

}