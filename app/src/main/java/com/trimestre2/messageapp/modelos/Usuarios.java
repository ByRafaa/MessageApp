package com.trimestre2.messageapp.modelos;

import android.net.Uri;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuarios implements Serializable {

    public String uid, email, nombre, telefono, provider;
    public ArrayList<Mensajes> listaMensajesRecibidos = new ArrayList<Mensajes>();
    public ArrayList<Mensajes> listaMensajesEnviados = new ArrayList<Mensajes>();

    public Usuarios() {

    }

    public Usuarios(String uid, String email){
        this.uid = uid;
        this.email = email;
    }

    public Usuarios(String uid,String email,String nombre,String telefono){

        this.uid = uid;
        this.email = email;
        this.nombre = nombre;
        this.telefono = telefono;

    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void addSentMessage(Mensajes mensaje) {
        listaMensajesEnviados.add(mensaje);
    }

    public void addRecMessage(Mensajes mensaje) {
        listaMensajesRecibidos.add(mensaje);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getProvider() {
        return provider;
    }

    public ArrayList<Mensajes> getListaMensajesRecibidos() {
        return listaMensajesRecibidos;
    }

    public void setListaMensajesRecibidos(ArrayList<Mensajes> listaMensajesRecibidos) {
        this.listaMensajesRecibidos = listaMensajesRecibidos;
    }

    public ArrayList<Mensajes> getListaMensajesEnviados() {
        return listaMensajesEnviados;
    }

    public void setListaMensajesEnviados(ArrayList<Mensajes> listaMensajesEnviados) {
        this.listaMensajesEnviados = listaMensajesEnviados;
    }
}
