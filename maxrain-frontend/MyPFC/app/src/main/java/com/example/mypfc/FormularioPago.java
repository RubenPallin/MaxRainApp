package com.example.mypfc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// FormularioPago.java
public class FormularioPago extends AppCompatActivity {

    EditText numeroTarjetaEditText, nombreCompletoEditText, mmAaEditText, cvcEditText;
    Button pagarAhoraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_pago);

        numeroTarjetaEditText = findViewById(R.id.numero_tarjeta_edit_text);
        nombreCompletoEditText = findViewById(R.id.nombre_completo_edit_text);
        mmAaEditText = findViewById(R.id.mm_aa_edit_text);
        cvcEditText = findViewById(R.id.cvc_edit_text);
        pagarAhoraButton = findViewById(R.id.boton_pagar_ahora);

        pagarAhoraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los datos del formulario
                String numeroTarjeta = numeroTarjetaEditText.getText().toString();
                String nombreCompleto = nombreCompletoEditText.getText().toString();
                String mmAa = mmAaEditText.getText().toString();
                String cvc = cvcEditText.getText().toString();

                // Validar los datos del formulario
                if (validarFormulario(numeroTarjeta, nombreCompleto, mmAa, cvc)) {
                    // Realizar el procesamiento del pago (simulando en este caso)
                    Toast.makeText(FormularioPago.this, "La compra fue un éxito", Toast.LENGTH_SHORT).show();
                } else {
                    // Mostrar un mensaje de error
                    Toast.makeText(FormularioPago.this, "Por favor, complete todos los campos correctamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validarFormulario(String numeroTarjeta, String nombreCompleto, String mmAa, String cvc) {
        // En este caso, simplemente verificamos si todos los campos están llenos.
        if (!numeroTarjeta.isEmpty() && !nombreCompleto.isEmpty() && !mmAa.isEmpty() && !cvc.isEmpty()) {
            // Mostrar un Toast con el mensaje de éxito
            Toast.makeText(FormularioPago.this, "Compra realizada exitosamente", Toast.LENGTH_SHORT).show();

            // Ir a la actividad MainMaxRain
            Intent mainIntent = new Intent(FormularioPago.this, MainMaxRain.class);
            startActivity(mainIntent);
            finish();

            return true;
        } else {
            // Mostrar un mensaje de error
            Toast.makeText(FormularioPago.this, "Por favor, complete todos los campos correctamente.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}