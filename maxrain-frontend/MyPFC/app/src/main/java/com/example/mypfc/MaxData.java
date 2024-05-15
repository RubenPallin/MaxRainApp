package com.example.mypfc;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MaxData {

    private String name;
    private int imageURL;
    private int codigoFamilia;

    private List<MaxData> subfamilies;

    public MaxData(String name, int imageURL, int codigoFamilia){
        this.name = name;
        this.imageURL = imageURL;
        this.codigoFamilia = codigoFamilia;
        this.subfamilies = new ArrayList<>();
    }

    public MaxData(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("descripcion_familia");
        this.imageURL = R.drawable.imagen; // Aquí asigna el recurso de imagen adecuado
        this.codigoFamilia = jsonObject.getInt("codigo_familia");
        this.subfamilies = new ArrayList<>(); // Inicializa la lista de subfamilias
    }

    public String getName() {
        return name;
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
}