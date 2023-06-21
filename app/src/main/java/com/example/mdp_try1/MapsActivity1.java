package com.example.mdp_try1;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mdp_try1.databinding.ActivityMaps1Binding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {
    double ulat,ulon,dlat,dlon;
    private GoogleMap mMap;
    private ActivityMaps1Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMaps1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String user=getIntent().getStringExtra("res_from_driver");
        DatabaseReference d= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        d.child("try1").child("user_reqs").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                user_req_obj rd=task.getResult().getValue(user_req_obj.class);
                ulon= Double.parseDouble(rd.ulon);
                ulat= Double.parseDouble(rd.ulat);
                dlon= Double.parseDouble(rd.dlon);
                dlat= Double.parseDouble(rd.dlat);
                Log.e("driver",rd.dlat);
                Log.e("user",rd.ulat);
                mapReady();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    public void mapReady() {
        // Add a marker in Sydney and move the camera
        LatLng usermap = new LatLng(ulat, ulon);
        mMap.addMarker(new MarkerOptions().position(usermap).title("Marker in User"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(user));
        LatLng drivermap = new LatLng(dlat, dlon);
        mMap.addMarker(new MarkerOptions().position(drivermap).title("Marker in Driver"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(drivermap));
    }
}