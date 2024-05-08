package com.example.mypfc;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

public class MaxData {
    private String name;
    private String imageURL;

    public MaxData(String name, String imageURL){
        this.name = name;
        this.imageURL = imageURL;
    }

    public MaxData(JSONObject jsonElement) throws JSONException{
        this.name = jsonElement.getString("descripcion_familia");
        this.imageURL = "drawable/imagen.png";
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }
}