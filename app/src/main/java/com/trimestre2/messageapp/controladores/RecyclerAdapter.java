package com.trimestre2.messageapp.controladores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trimestre2.messageapp.R;
import com.trimestre2.messageapp.modelos.Usuarios;
import com.trimestre2.messageapp.vistas.Chat;
import com.trimestre2.messageapp.vistas.Home;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.UsuariosViewHolder> implements View.OnClickListener {

    ArrayList<Usuarios> listaUsuarios;
    private Context context;

    public RecyclerAdapter(ArrayList<Usuarios> listaUsuarios){

        this.listaUsuarios = listaUsuarios;
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,null,false);
        return new UsuariosViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {

        holder.asignarDatos(listaUsuarios.get(position));

        Usuarios usuario = listaUsuarios.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,Chat.class);
                intent.putExtra("email",usuario.email);
                intent.putExtra("nombre",usuario.nombre);
                intent.putExtra("telefono",usuario.telefono);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    @Override
    public void onClick(View v) {



    }

    public class UsuariosViewHolder extends RecyclerView.ViewHolder {

        TextView email,nombre,telefono,provider;


        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            email = itemView.findViewById(R.id.emailField);
            nombre = itemView.findViewById(R.id.nombreField);
            telefono = itemView.findViewById(R.id.telefonoField);
            provider = itemView.findViewById(R.id.providerField);
        }

        public void asignarDatos(Usuarios usuarios) {

            email.setText(usuarios.email);
            nombre.setText(usuarios.nombre);
            telefono.setText(usuarios.telefono);
            provider.setText(usuarios.provider);

        }
    }
}
