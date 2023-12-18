package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    EditText Email,Pass;
    Button Login;
    TextView Reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login_page);
        Email = findViewById(R.id.Email);
        Pass = findViewById(R.id.Pass);
        Login = findViewById(R.id.Login);
        Reg = findViewById(R.id.Reg);

    }
    public void Login(View view) {
                if(!Email.getText().toString().isEmpty() && !Pass.getText().toString().isEmpty()){
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(Email.getText().toString(),Pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(LoginPage.this,BloodDonors.class));
                                    }else {
                                        Toast.makeText(LoginPage.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
        }
    }
    public void reg (View view){
        startActivity(new Intent(LoginPage.this,RegisterPage.class));
    }
}