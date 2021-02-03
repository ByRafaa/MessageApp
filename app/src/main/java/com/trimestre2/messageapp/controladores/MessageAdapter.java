package com.trimestre2.messageapp.controladores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trimestre2.messageapp.R;
import com.trimestre2.messageapp.modelos.Mensajes;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    ArrayList<Mensajes> listaMensajes;

    public MessageAdapter(ArrayList<Mensajes> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    public void enviar(Mensajes mensaje) {

        listaMensajes.add(mensaje);
        notifyItemInserted(listaMensajes.size());

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages,null,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        holder.asignarDatos(listaMensajes.get(position));

    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mensajeView,nombreView,fechaView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mensajeView=itemView.findViewById(R.id.idMessage);
            nombreView=itemView.findViewById(R.id.idName);
            fechaView=itemView.findViewById(R.id.idDate);
        }

        public void asignarDatos(Mensajes mensajes) {

            mensajeView.setText(mensajes.mensaje);
            nombreView.setText(mensajes.nombre);
            fechaView.setText(mensajes.fecha.toString());


        }
    }
}
