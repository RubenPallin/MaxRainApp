package com.example.mypfc;

import org.json.JSONException;
import org.json.JSONObject;

public class UsuariosData {

    private String nombre;
    private String apellido;
    private String contraseña;
    private String email;
    private String telefono;

    // Constructor
    public UsuariosData(String nombre, String apellido, String contraseña, String email, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.contraseña = contraseña;
        this.email = email;
        this.telefono = telefono;
    }

    // Constructor que acepta un JSONObject
    public UsuariosData(JSONObject jsonObject) throws JSONException {
        this.nombre = jsonObject.getString("nombre");
        this.apellido = jsonObject.getString("apellido");
        this.contraseña = jsonObject.getString("contraseña");
        this.email = jsonObject.getString("email");
        this.telefono = jsonObject.optString("telefono", null); // Uso de optString para manejar el campo opcional
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
