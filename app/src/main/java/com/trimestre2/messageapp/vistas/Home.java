package com.trimestre2.messageapp.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trimestre2.messageapp.R;
import com.trimestre2.messageapp.controladores.RecyclerAdapter;
import com.trimestre2.messageapp.modelos.Usuarios;

import java.util.ArrayList;
import java.util.Iterator;

public class Home extends AppCompatActivity {

    public static ArrayList<Usuarios> listaUsuarios;
    RecyclerView recycler;

    public static Usuarios currentUser;

    String nombre;
    String telefono;

    FirebaseAuth auth;
    FloatingActionButton logOut;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getExtras();

        //HOOKS
        recycler = findViewById(R.id.recycler);
        logOut = findViewById(R.id.logOutBut);

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        this.setTitle(usuario.getEmail());

        if (bundle!=null){

            nombre = bundle.getString("nombre");
            telefono = bundle.getString("telefono");
            auth = FirebaseAuth.getInstance();

        }

        recycler.setLayoutManager(new LinearLayoutManager(this));

        listaUsuarios = new ArrayList<Usuarios>();
        database = FirebaseDatabase.getInstance().getReference().child("users");

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                finish();

            }
        });
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot xUser : snapshot.getChildren()){

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        Usuarios usuario = xUser.getValue(Usuarios.class);

                        if (user.getEmail().equals(usuario.email)){

                            currentUser = xUser.getValue(Usuarios.class);

                        } else {

                            listaUsuarios.add(usuario);
                            RecyclerAdapter adapter = new RecyclerAdapter(listaUsuarios);
                            recycler.setAdapter(adapter);
                            recycler.addItemDecoration( new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            database.addValueEventListener(eventListener);


        }


}