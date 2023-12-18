package com.example.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {
    EditText Name, editTextTextEmailAddress2, editTextPhone, editTextTextPassword2;
    Button button2, back;
    Spinner spinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register_page);
        Name = findViewById(R.id.Name);
        editTextTextEmailAddress2 = findViewById(R.id.editTextTextEmailAddress2);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2);
        button2 = findViewById(R.id.button2);
        back = findViewById(R.id.back);
        spinner = findViewById(R.id.spinner);

    }

    public void register (View view) {
        String name, email, phone, pass;
        name = Name.getText().toString();
        email = editTextTextEmailAddress2.getText().toString();
        phone = editTextPhone.getText().toString();
        pass = editTextTextPassword2.getText().toString();

        if (name.isEmpty()) {
            Name.setError("Fill this field.");
        }
        if (email.isEmpty()) {
            editTextTextEmailAddress2.setError("Fill this field.");
        }
        if (phone.isEmpty()) {
            editTextPhone.setError("Fill this field.");
        }
        if (pass.isEmpty()) {
            editTextTextPassword2.setError("Fill this field.");
        }
            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !pass.isEmpty()) {
                RegisterUser(name, email, phone, pass);
            }
        }

    private void RegisterUser(String name, String email, String phone, String pass) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addToDatabase(task.getResult().getUser().getUid(), name, email, phone, pass);
                    } else {
                        Toast.makeText(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addToDatabase(String uid, String name, String email, String phone, String pass) {
        HashMap<String, String> values = new HashMap<>();
        values.put("Name", name);
        values.put("editTextTextEmailAddress2", email);
        values.put(" editTextPhone", phone);
        values.put("  editTextTextPassword2", pass);
        values.put("UID", uid);
        values.put("Step","1");
        values.put("Visible","False");


        FirebaseDatabase.getInstance().getReference("Donors")
                .child(uid).setValue(values).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(RegisterPage.this, LoginPage.class));
            }
        });
    }
}



