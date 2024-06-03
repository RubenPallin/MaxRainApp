package com.example.mypfc;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MaxData {

    private String nombre;
    private int imageURL;
    private int codigoFamilia;

    private List<MaxData> subfamilies;

    public MaxData(String nombre, int imageURL, int codigoFamilia){
        this.nombre = nombre;
        this.imageURL = imageURL;
        this.codigoFamilia = codigoFamilia;
        this.subfamilies = new ArrayList<>();
    }

    public MaxData(JSONObject jsonObject) throws JSONException {
        this.nombre = jsonObject.getString("descripcion_familia");
        this.imageURL = obtenerImagenFamilia(jsonObject.getInt("codigo_familia"));
        this.codigoFamilia = jsonObject.getInt("codigo_familia");
        this.subfamilies = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getImageURL() {
        return imageURL;
    }

    public List<MaxData> getSubfamilies() {
        return subfamilies;
    }

    public void addSubfamily(MaxData subfamily) {
        subfamilies.add(subfamily);
    }

    public int getCodigoFamilia() {
        return codigoFamilia;
    }
    // Método para obtener la imagen de la familia
    private int obtenerImagenFamilia(int codigoFamilia) {
        switch (codigoFamilia) {
            case 1:
                return R.drawable.krain_fam;
            case 2:
                return R.drawable.urbatec;
            case 3:
                return R.drawable.brico;
            case 4:
                return R.drawable.rainbird;
            case 5:
                return R.drawable.solem_fam;
            case 6:
                return R.drawable.hit_fam;
            case 7:
                return R.drawable.teco;
            case 8:
                return R.drawable.max_rain_brico;
            case 9:
                return R.drawable.orework;
            case 10:
                return R.drawable.dripop;
            case 11:
                return R.drawable.azud;
            case 12:
                return R.drawable.jimtem;

            default:
                return R.drawable.imagen; // Imagen predeterminada en caso de que no haya ninguna coincidencia
        }
    }

    // Otros métodos getter y setter
}