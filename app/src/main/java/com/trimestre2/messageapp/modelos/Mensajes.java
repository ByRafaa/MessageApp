package com.trimestre2.messageapp.modelos;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Mensajes implements Serializable {

    public String uid;
    public String mensaje;
    public String nombre;
    public Date fecha;

    public Mensajes(String uid,String mensaje,String nombre,Date fecha){

        this.uid = uid;
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fecha = fecha;

    }

    public Mensajes(String uid,String mensaje,Date fecha){

        this.uid = uid;
        this.mensaje = mensaje;
        this.fecha = fecha;

    }

    public static Comparator<Mensajes> ordenarLista = new Comparator<Mensajes>() {
        @Override
        public int compare(Mensajes o1, Mensajes o2) {

            Date fecha1 = o1.fecha;
            Date fecha2 = o2.fecha;

            return fecha1.compareTo(fecha2);

        }
    };

    public Mensajes(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
