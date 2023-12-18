package com.example.bloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class BloodDonors extends AppCompatActivity {
    FloatingActionButton fab;
    String fname, phonen, bloodg, padr;
    ListView lv;
    ArrayList<Lvitems> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_blood_donors);
        lv = findViewById(R.id.lv);
        fab = findViewById(R.id.fab);

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // Handle the case where the user is not logged in
            // You might want to redirect to the login page or take appropriate action
            startActivity(new Intent(this, LoginPage.class));
            finish();
            return;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up Firebase reference to the "donors" node
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("donors");

                // Check if the user is already registered as a donor
                databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User has already registered as a donor
                            Toast.makeText(BloodDonors.this, "You are already registered as a donor", Toast.LENGTH_SHORT).show();
                        } else {
                            // User is not registered, proceed with the registration process
                            openDonorRegistrationDialog(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors
                        Log.e("FirebaseDatabase", "Error: " + databaseError.getMessage());
                    }
                });
            }
        });

        // Set up Firebase reference to the "donors" node
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("donors");

        // Add ValueEventListener to retrieve donor details
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing list
                arrayList.clear();

                // Iterate through the dataSnapshot to get all donors' details
                for (DataSnapshot donorSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot itemSnapshot : donorSnapshot.getChildren()) {
                        Lvitems lvItem = itemSnapshot.getValue(Lvitems.class);
                        arrayList.add(lvItem);
                    }
                }

                // Notify the adapter of the data change
                ContactAdapter contactAdapter = new ContactAdapter(arrayList, BloodDonors.this);
                lv.setAdapter(contactAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.e("FirebaseDatabase", "Error: " + databaseError.getMessage());
            }
        });
    }

    private void storeDonorDetails(String userId, Lvitems lvitems) {
        // Get a reference to the Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("donors");

        // Store donor details under the user's UID in the "donors" node
        databaseReference.child(userId).push().setValue(lvitems);
    }

    // Move the registration logic here to handle the case when the user is not already registered
    private void openDonorRegistrationDialog(FirebaseUser user) {
        final Dialog dialog = new Dialog(BloodDonors.this);
        dialog.setContentView(R.layout.fabitems);
        final EditText name = dialog.findViewById(R.id.name);
        final EditText phone = dialog.findViewById(R.id.phone);
        final EditText blood = dialog.findViewById(R.id.blood);
        final EditText pa = dialog.findViewById(R.id.pa);
        Button save = dialog.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fname = name.getText().toString();
                phonen = phone.getText().toString();
                bloodg = blood.getText().toString();
                padr = pa.getText().toString();

                if (fname.isEmpty() || phonen.isEmpty() || bloodg.isEmpty() || padr.isEmpty()) {
                    // Display a toast message if any field is empty
                    Toast.makeText(BloodDonors.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    // All fields are filled, proceed with registration
                    Lvitems lvitems = new Lvitems();
                    lvitems.setName(fname);
                    lvitems.setPhone(phonen);
                    lvitems.setBlood(bloodg);
                    lvitems.setPa(padr);

                    // Store donor details in the database
                    storeDonorDetails(user.getUid(), lvitems);

                    arrayList.add(lvitems);
                    dialog.dismiss();
                    ContactAdapter contactAdapter = new ContactAdapter(arrayList, BloodDonors.this);
                    lv.setAdapter(contactAdapter);
                    Toast.makeText(BloodDonors.this, "Congratulations! You have registered as a DONOR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
