package com.example.mypfc;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticulosData  {

    private String codigoArticulo;
    private String nombre;
    private int imageURL;
    private String codigoFamilia;
    private String codigoMarca;
    private double precio;

    public ArticulosData(String codigoArticulo, String nombre, int imageURL, String codigoFamilia, String codigoMarca, double precio) {
        this.codigoArticulo = codigoArticulo;
        this.nombre = nombre;
        this.imageURL = imageURL;
        this.codigoFamilia = codigoFamilia;
        this.codigoMarca = codigoMarca;
        this.precio = precio;
    }

    public ArticulosData(JSONObject jsonObject) throws JSONException {
        this.codigoArticulo = jsonObject.getString("codigo_articulo");
        this.nombre = jsonObject.getString("descripcion");
        this.imageURL =  R.drawable.imagen;
        this.precio = jsonObject.getDouble("precio");
    }

    protected ArticulosData(Parcel in) {
        codigoArticulo = in.readString();
        nombre = in.readString();
        imageURL = in.readInt();
        codigoFamilia = in.readString();
        codigoMarca = in.readString();
        precio = in.readDouble();
    }
    public static final Parcelable.Creator<ArticulosData> CREATOR = new Parcelable.Creator<ArticulosData>() {
        @Override
        public ArticulosData createFromParcel(Parcel in) {
            return new ArticulosData(in);
        }

        @Override
        public ArticulosData[] newArray(int size) {
            return new ArticulosData[size];
        }
    };

    public String getCodigoArticulo() {
        return codigoArticulo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getImageURL() {
        return imageURL;
    }

    public String getCodigoFamilia() {
        return codigoFamilia;
    }

    public String getCodigoMarca() {
        return codigoMarca;
    }

    public double getPrecio() {
        return precio;
    }
}