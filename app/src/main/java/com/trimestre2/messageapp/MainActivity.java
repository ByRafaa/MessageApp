package com.trimestre2.messageapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trimestre2.messageapp.modelos.Usuarios;
import com.trimestre2.messageapp.vistas.Chat;
import com.trimestre2.messageapp.vistas.Home;

import static java.sql.DriverManager.println;

public class MainActivity extends AppCompatActivity {

    Button login;
    Button register;
    SignInButton google;
    TextView email;
    TextView pass;
    Context context;
    Button confirmBut;
    ConstraintLayout registerLayout;
    ConstraintLayout confirmLayout;
    TextView nombre;
    TextView telefono;
    FloatingActionButton back;

    DatabaseReference database;

    private final int GOOGLE_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //HOOKS
        context = this;
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        google = findViewById(R.id.google);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        confirmBut = findViewById(R.id.confirmBut);
        registerLayout = findViewById(R.id.registerLayout);
        confirmLayout = findViewById(R.id.confirmLayout);
        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        back = findViewById(R.id.backBut);
        //INIT
        database = FirebaseDatabase.getInstance().getReference();

        confirmLayout.setVisibility(View.INVISIBLE);
        registerLayout.setVisibility(View.VISIBLE);

        setup();

    }

    private void setup() {

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {

                    Toast.makeText(context, "Rellene los campos de arriba con el correo y la contraseña con los que desea registrarse.", Toast.LENGTH_LONG).show();

                } else {

                    registerLayout.setVisibility(View.INVISIBLE);
                    confirmLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombre.getText().toString().isEmpty() || telefono.getText().toString().isEmpty()) {

                    Toast.makeText(context, "No puede haber campos vacíos", Toast.LENGTH_LONG).show();

                } else {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    confirmLayout.setVisibility(View.INVISIBLE);
                                    registerLayout.setVisibility(View.VISIBLE);

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    Usuarios usuario = new Usuarios(user.getUid(), user.getEmail(),nombre.getText().toString(),telefono.getText().toString());

                                    usuario.setProvider("E-mail");

                                    database.child("users").child(user.getUid()).setValue(usuario);

                                    persistencia();
                                    sesion(nombre.getText().toString(),telefono.getText().toString());

                                    nombre.setText("");
                                    telefono.setText("");

                                } else {

                                    confirmLayout.setVisibility(View.INVISIBLE);
                                    registerLayout.setVisibility(View.VISIBLE);

                                    Toast.makeText(context, "No se ha podido registrar. Ya existe una cuenta con sus credenciales.", Toast.LENGTH_LONG).show();

                                }
                            }
                    });

                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {

                    Toast.makeText(context, "No puede haber campos vacíos", Toast.LENGTH_LONG).show();

                } else {

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user.getUid().equals(database.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (snapshot.exists()){

                                            //database.child("users").child(user.getUid()).setValue(snapshot.getValue(Usuarios.class));

                                            System.out.println("EL USUARIO EXISTE");

                                        } else {

                                            Usuarios usuario = new Usuarios(user.getUid(), user.getEmail(),user.getDisplayName(),user.getPhoneNumber());

                                            usuario.setProvider("E-Mail");

                                            database.child("users").child(user.getUid()).setValue(usuario);

                                        }

                                        persistencia();
                                        sesion(user.getDisplayName(),user.getPhoneNumber());

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }))){

                                }

                            } else {

                                Toast.makeText(context, "No se ha podido conectar con su cuenta.", Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

                GoogleSignInClient googleClient = GoogleSignIn.getClient(getApplicationContext(), gso);

                googleClient.signOut();

                Intent signInIntent = googleClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmLayout.setVisibility(View.INVISIBLE);
                registerLayout.setVisibility(View.VISIBLE);

                nombre.setText("");
                telefono.setText("");

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            GoogleSignInAccount acc = null;
            try {
                acc = task.getResult(ApiException.class);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                println(e.getMessage());
            }

            if (acc != null) {

                AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);

                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.getUid().equals(database.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()){

                                        //database.child("users").child(user.getUid()).setValue(snapshot.getValue(Usuarios.class));

                                        System.out.println("EL USUARIO EXISTE");

                                    } else {

                                        Usuarios usuario = new Usuarios(user.getUid(), user.getEmail(),user.getDisplayName(),user.getPhoneNumber());

                                        usuario.setProvider("Google");

                                        database.child("users").child(user.getUid()).setValue(usuario);

                                    }

                                    persistencia();
                                    sesion(user.getDisplayName(),user.getPhoneNumber());

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }))){

                            }

                        } else {

                            Toast.makeText(context, "No se ha podido conectar con su cuenta.", Toast.LENGTH_LONG).show();

                            confirmLayout.setVisibility(View.INVISIBLE);
                            registerLayout.setVisibility(View.VISIBLE);

                        }

                    }
                });


            }

        }
    }
    public void sesion(String nombre, String telefono) {

        SharedPreferences prefs = getSharedPreferences("com.trimestre2.messenger.FILE", Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);

        if (email == null) {

            //EN ESTE CASO SIGNIFICARIA QUE NO TENEMOS SESION INICIADA, YA QUE NO HAY NINGUN EMAIL EN EL FICHERO

        } else {

            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("nombre",nombre);
            intent.putExtra("telefono",telefono);
            startActivity(intent);

        }

    }

    public void persistencia() {

        SharedPreferences.Editor prefs = getSharedPreferences("com.trimestre2.messenger.FILE", Context.MODE_PRIVATE).edit();
        prefs.putString("email", email.getText().toString());
        prefs.apply();

    }
}