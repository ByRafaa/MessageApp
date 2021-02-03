package com.trimestre2.messageapp.vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trimestre2.messageapp.R;
import com.trimestre2.messageapp.controladores.MessageAdapter;
import com.trimestre2.messageapp.modelos.Mensajes;
import com.trimestre2.messageapp.modelos.Usuarios;

import java.util.ArrayList;
import java.util.Calendar;

public class Chat extends AppCompatActivity {


    RecyclerView recyclerSent;

    ArrayList<Mensajes> listaMensajesEnviados = new ArrayList<Mensajes>();
    ArrayList<Mensajes> listaMensajesRecibidos = new ArrayList<Mensajes>();

    TextView nombre,telefono,email,chat;

    Button send;

    MessageAdapter adapterSent;

    DatabaseReference database;

    Usuarios currentUser;
    Usuarios receptUser;

    RecyclerView.Adapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //HOOKS
        nombre = findViewById(R.id.nameChat);
        telefono = findViewById(R.id.numberChat);
        email = findViewById(R.id.emailChat);
        chat = findViewById(R.id.chat);
        send = findViewById(R.id.sendBut);
        recyclerSent = findViewById(R.id.recyclerIdSent);

        adapterSent = new MessageAdapter(listaMensajesEnviados);

        recyclerSent.setLayoutManager(new LinearLayoutManager(this));

        recyclerSent.setAdapter(adapterSent);

        currentUser = Home.currentUser;

        database = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();

        nombre.setText(bundle.getString("nombre"));
        telefono.setText(bundle.getString("telefono"));
        email.setText(bundle.getString("email"));
        receptUser = obtenerUsuario(email.getText().toString());

        cargarMensajesEnviados();
        cargarMensajesRecibidos();

        setup();

    }

    private void setup() {

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    crearMensaje();

                    cargarMensajesEnviados();

                    cargarMensajesRecibidos();

            }
        });
    }

    private Usuarios obtenerUsuario(String email) {

        Usuarios user = new Usuarios();

        for (int i = 0;i<Home.listaUsuarios.size();i++){

            if (Home.listaUsuarios.get(i).email.equals(email)){

                user = Home.listaUsuarios.get(i);

            }
        }

        return user;

    }

    private void crearMensaje(){

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Mensajes mensaje = new Mensajes(receptUser.getUid(),chat.getText().toString(),currentUser.getNombre(), Calendar.getInstance().getTime());

                listaMensajesEnviados.add(mensaje);

                listaMensajesEnviados.sort(Mensajes.ordenarLista);

                database.child("users").child(currentUser.getUid()).child("sentMessages").child(receptUser.getUid()).push().setValue(mensaje);

                Mensajes mensaje_2 = new Mensajes(currentUser.getUid(),chat.getText().toString(),receptUser.getNombre(),Calendar.getInstance().getTime());

                listaMensajesRecibidos.add(mensaje_2);

                database.child("users").child(receptUser.getUid()).child("receivedMessages").child(currentUser.getUid()).push().setValue(mensaje_2);

                chat.setText("");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void cargarMensajesEnviados(){

        database.child("users").child(currentUser.getUid()).child("sentMessages").child(receptUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Mensajes mensaje = snapshot.getValue(Mensajes.class);

                mensaje.setNombre("");

                adapterSent.enviar(mensaje);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void cargarMensajesRecibidos(){

        database.child("users").child(currentUser.getUid()).child("receivedMessages").child(receptUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Mensajes mensaje_2 = snapshot.getValue(Mensajes.class);
                adapterSent.enviar(mensaje_2);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}