package com.example.mypfc;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoAdapter extends RecyclerView.Adapter<CarritoViewHolder> {

    private List<ArticulosData> articulosList;
    private Context context;
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_CART_ITEMS = "CartItems";
    static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    static final String KEY_TOKEN = "token";
    public CarritoAdapter(List<ArticulosData> articulosList, Context context) {
        this.articulosList = articulosList;
        this.context = context;
    }

    @NonNull
    @Override
    public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_carrito_view_holder, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarritoViewHolder holder, int position) {
        ArticulosData articulo = articulosList.get(position);
        holder.bindCarrito(articulo, position, this);
    }

    @Override
    public int getItemCount() {
        return articulosList.size();
    }

    // Método para eliminar un artículo del carrito
    public void eliminarArticuloDelCarrito(final String codigoArticulo, final int position) {
        // Obtener el token de sesión
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String sessionToken = sharedPreferences.getString(KEY_TOKEN, "");

        // URL del servidor para eliminar el artículo del carrito
        String url = "http://10.0.2.2:8000/carrito/" + codigoArticulo;

        // Crear la solicitud DELETE
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsear la respuesta JSON
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("message")) {
                                // Mostrar mensaje de éxito al usuario
                                Toast.makeText(context, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();

                                // Remover el artículo de la lista local
                                articulosList.remove(position);

                                // Notificar al adaptador del RecyclerView sobre el cambio
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, articulosList.size());
                            } else {
                                // Manejar el caso donde no se recibe el mensaje esperado
                                Toast.makeText(context, "Error inesperado al eliminar el artículo", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, "Error al eliminar el artículo del carrito", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Agregar el token de sesión como encabezado de autorización
                Map<String, String> headers = new HashMap<>();
                headers.put("token", sessionToken);
                return headers;
            }
        };

        // Obtener la instancia de Volley y agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
