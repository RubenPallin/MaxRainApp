package com.example.mypfc;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MisDatos extends AppCompatActivity {
    private Context context = this;
    private EditText editarNombre, editarApellido, editarEmail, editarTelefono, editarContraseña;
    private TextView mostrarContraseña;
    private boolean hide;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mis_datos);

        editarNombre = findViewById(R.id.editar_nombre);
        editarApellido = findViewById(R.id.editar_apellido);
        editarContraseña = findViewById(R.id.editar_contraseña);
        editarEmail = findViewById(R.id.editar_email);
        editarTelefono = findViewById(R.id.editar_telefono);
        mostrarContraseña = findViewById(R.id.mostrar);
        mostrarContraseña.setOnClickListener(showListener);

        Toolbar toolbarDatos = findViewById(R.id.toolbar_mis_datos);

        setSupportActionBar(toolbarDatos);

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

        obtenerDatosPersonales();
    }

    private void obtenerDatosPersonales(){
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String sessionToken = sharedPreferences.getString(KEY_TOKEN, "");

        if (sessionToken == null || sessionToken.isEmpty()) {
            Toast.makeText(this, "Token no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }


        Log.d("Token", "Token: " + sessionToken);

        String url= "http://10.0.2.2:8000/usuario/";

        // Hacer la solicitud GET para obtener los datos del usuario
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta JSON y actualizar la interfaz de usuario
                        try {
                            UsuariosData usuariosData = parsearRespuesta(response);
                            mostrarDatosUsuario(usuariosData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar errores de la solicitud
                if (error.networkResponse == null) {
                    Toast.makeText(context, "No se ha establecido la conexión", Toast.LENGTH_SHORT).show();
                } else {
                    int serverCode = error.networkResponse.statusCode;
                    Toast.makeText(context, "Estado del servidor: " + serverCode, Toast.LENGTH_SHORT).show();
                }
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("token", sessionToken);
                return headers;
            }};

        // Obtener la instancia de Volley y agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // Método para convertir la respuesta JSON en un objeto Usuario
    private UsuariosData parsearRespuesta(JSONObject response) throws Exception {
        String nombre = response.getString("nombre");
        String apellido = response.getString("apellido");
        String contraseña = response.getString("contraseña");
        String email = response.getString("email");
        String telefono = response.optString("telefono", null);

        return new UsuariosData(nombre, apellido, contraseña, email, telefono);
    }

    // Método para mostrar los datos del usuario en la interfaz de usuario
    private void mostrarDatosUsuario(UsuariosData usuario) {
        editarNombre.setText(usuario.getNombre());
        editarApellido.setText(usuario.getApellido());
        editarContraseña.setText(usuario.getContraseña());
        editarEmail.setText(usuario.getEmail());
        editarTelefono.setText(usuario.getTelefono());

        // Deshabilitar la edición de campos
        editarNombre.setEnabled(false);
        editarApellido.setEnabled(false);
        editarContraseña.setEnabled(false);
        editarEmail.setEnabled(false);
        editarTelefono.setEnabled(false);
    }

    //Método para mostrar la contraseña
    private View.OnClickListener showListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (hide) {
                hide = false;
                editarContraseña.setTransformationMethod(null);
            } else {
                hide = true;
                editarContraseña.setTransformationMethod(new PasswordTransformationMethod());
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
