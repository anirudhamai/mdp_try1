package com.example.mdp_try1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_window extends AppCompatActivity {
    TextView t1,t2,t3,t4;
    DatabaseReference wdatabase,wdatabase1;
    String user;
    double latitude,longitude;
    user_req_obj res;
    private ProgressDialog progressDialog;
    private ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_window);

        user = getIntent().getStringExtra("uname").toString();
        latitude = Double.valueOf(getIntent().getStringExtra("lat"));
        longitude = Double.valueOf(getIntent().getStringExtra("lon"));

        t1=(TextView) findViewById(R.id.t1);
        t2=(TextView) findViewById(R.id.t2);
        t3=(TextView) findViewById(R.id.t3);

        wdatabase= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        wdatabase.child("try1").child("user_request").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    res= task.getResult().getValue(user_req_obj.class);
                    if("NA".equals(res.driver)){
                        wdatabase1= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

                        // Create and show the ProgressDialog
                        progressDialog = new ProgressDialog(user_window.this);
                        progressDialog.setMessage("Waiting for driver...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        waitForFieldValueUpdate("served",1);
                    }else {
                        Toast.makeText(user_window.this, "Ambulance is on the way!!", Toast.LENGTH_SHORT).show();
                        t3.setText(res.driver);
                        t2.setText(res.eta);
                        t1.setText(res.user);
                    }
                }
            }
        });
    }

    private void waitForFieldValueUpdate(final String field, final int desiredValue) {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.child("user_reqs").child(user).child(field).getValue(Integer.class);
                if (value != 0 && value==desiredValue) {
                    // Desired value is updated in the database field
                    progressDialog.dismiss();
                    Toast.makeText(user_window.this, "Ambulance is on the way!!", Toast.LENGTH_SHORT).show();
                    wdatabase1.removeEventListener(valueEventListener);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error occurred while reading from the database
                progressDialog.dismiss();
                Toast.makeText(user_window.this, "Failed to read database", Toast.LENGTH_SHORT).show();
            }
        };

        // Add the ValueEventListener to the DatabaseReference
        wdatabase1.addValueEventListener(valueEventListener);
    }
}