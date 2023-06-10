package com.example.mdp_try1;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class driver1 extends AppCompatActivity {
    int locfetched=0;
    String user;
    DatabaseReference wdatabase;
    private Handler handler;
    private Runnable toggleChecker;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    final String[] loc = new String[2];

    ToggleButton avail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver1);
        user = getIntent().getStringExtra("uname").toString();

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        avail=(ToggleButton) findViewById(R.id.toggleButton);
        avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(avail.isChecked())
                {
                    avail.setBackgroundColor(GREEN);
                    avail.setRotationX(-13);
                }
                else{
                    avail.setBackgroundColor(RED);
                    avail.setRotationX(13);
                }
            }
        });
        fun();
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(toggleChecker, 30000); // Start the initial check after 10 seconds
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(toggleChecker); // Stop the continuous checking
    }


    public void fun()
    {
        Log.e("inside","fun");

        handler = new Handler();
        toggleChecker = new Runnable() {
            @Override
            public void run() {
                Log.e("inside","run");
                checkToggleButtonState(); // Method to check the ToggleButton state
                handler.postDelayed(this, 30000); // Schedule the next check after 10 seconds (10,000 milliseconds)
            }
        };
    }

    public void checkToggleButtonState()
    {
        boolean isChecked = avail.isChecked();

        if (isChecked) {
            Log.e("state","available");
            locationTrack = new LocationTrack(driver1.this);
            if (locationTrack.canGetLocation()) {
                double longitude = locationTrack.getLongitude();
                double latitude = locationTrack.getLatitude();
                try {
                    if(longitude==0.0)
                    {
                        Thread.sleep(1000);
                        locfetched=0;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(longitude!=0.0)
                {
                    locfetched=1;
                }
                loc[0] = String.valueOf(longitude);
                loc[1] = String.valueOf(latitude);
                Log.e("lon",loc[0]);
                Log.e("lat",loc[1]);
                readydriver r1=new readydriver(loc[1],loc[0],user);
                wdatabase= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                wdatabase.child("try1").child("ready_drivers").child(user).setValue(r1);
//                wdatabase.child("try1").child("ready_drivers").child(user).child("lat").setValue(loc[1]);
//                wdatabase.child("try1").child("ready_drivers").child(user).child("user").setValue(user);
            }
            else {
                locfetched=0;
//                locationTrack.showSettingsAlert();

            }
        } else {
            Log.e("state", "busy");
            wdatabase = FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
            if (locfetched == 0) {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT);
            } else {
                //remove driver data
                wdatabase.child("try1").child("ready_drivers").child(user).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
            }
        }
    }




    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}