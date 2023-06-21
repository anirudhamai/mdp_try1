package com.example.mdp_try1;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mdp_try1.databinding.ActivityUserMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_maps extends FragmentActivity implements OnMapReadyCallback {
    private TextView t1,t2,t3;
    DatabaseReference wdatabase;
    String user,driver,eta;
    double ulat,ulon,dlat,dlon;
    int i=1;
    user_req_obj res;


    private GoogleMap mMap;
    private ActivityUserMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        driver = getIntent().getStringExtra("driver");
//        eta = getIntent().getStringExtra("eta");
//        user = getIntent().getStringExtra("user");
//       res=(user_req_obj)getIntent().getSerializableExtra("res");
        user=getIntent().getStringExtra("res");
        CardView cardView = findViewById(R.id.cv);
        t1= cardView.findViewById(R.id.textView1);
        t2=cardView.findViewById(R.id.textView2);
        t3=cardView.findViewById(R.id.textView3);

        t3.setText("res.getname()");
        t2.setText("NA");
        t1.setText("NA");

//        t3.setText(res.driver);
//        t2.setText(res.eta);
//        t1.setText(res.user);

        wdatabase= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        wdatabase.child("try1").child("user_reqs").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {

                    res= task.getResult().getValue(user_req_obj.class);
                    dlat=Double.valueOf(res.dlat);
                    dlon=Double.valueOf(res.dlon);
                    ulat=Double.valueOf(res.ulat);
                    ulon=Double.valueOf(res.ulon);
                    Log.e("lat", String.valueOf(dlat));
                    t3.setText("res.getname()");
                    t2.setText("NA");
                    t1.setText("NA");
                    i=1;
                }
            }
        });
        wdatabase = FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        wdatabase.child("try1").child("user_reqs").child(user).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.e("status", "deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("status", "deletion failed");
                }
            });
            Toast.makeText(this, "Table Updated", Toast.LENGTH_SHORT);



        binding = ActivityUserMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(i==1)
        {
            LatLng driv = new LatLng( dlat, dlon);
            mMap.addMarker(new MarkerOptions().position(driv).title("Driver"));
            LatLng user = new LatLng( ulat, ulon);
            mMap.addMarker(new MarkerOptions().position(driv).title("User"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(driv));
        }

        // Add a marker in Sydney and move the camera

    }


}