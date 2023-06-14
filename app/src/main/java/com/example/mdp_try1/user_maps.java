package com.example.mdp_try1;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class user_maps extends FragmentActivity implements OnMapReadyCallback {
    TextView t1,t2,t3,t4;
    DatabaseReference wdatabase;
    String user,driver,eta;
    double latitude,longitude,ulat,ulon,dlat,dlan;
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
       res=(user_req_obj)getIntent().getSerializableExtra("res");


        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView1);
        t3=(TextView) findViewById(R.id.textView2);

        t3.setText(res.driver);
        t2.setText(res.eta);
        t1.setText(res.user);
        latitude=Double.valueOf(res.dlat);
        longitude=Double.valueOf(res.dlon);


        binding = ActivityUserMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(i==1)
        {
            LatLng sydney = new LatLng( latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in You"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        // Add a marker in Sydney and move the camera

    }


}