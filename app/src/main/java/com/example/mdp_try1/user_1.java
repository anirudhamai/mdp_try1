package com.example.mdp_try1;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

public class user_1 extends AppCompatActivity {

    Button getloc, book;
    String user;
    int locfetched = 0, loopend=0;
    double longitude,latitude;
    user_req_obj res,check;
    DatabaseReference wdatabase,wdatabase1,wb;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    final String[] loc = new String[2];
    private ProgressDialog progressDialog;
    private Handler handler;
    private Runnable toggleChecker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user1);

        user = getIntent().getStringExtra("uname").toString();

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        getloc=(Button)findViewById(R.id.button);
        getloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getloc();
            }
        });

        book=(Button)findViewById(R.id.button2);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locfetched==1) {
                    func(user);
                }
                else {
                    new AlertDialog.Builder(user_1.this)
                            .setMessage("Please fetch your device location")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                    return;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                wb= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                wb.child("try1").child("user_reqs").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Log.e("wait","Firebase waiting");
                            res= task.getResult().getValue(user_req_obj.class);
                            Log.e("res",res.driver);

                            if("NA".equals(res.driver)){
                                Log.e("WATCH","inside fetching");
                                // Create and show the ProgressDialog
                                progressDialog = new ProgressDialog(user_1.this);
                                progressDialog.setMessage("Waiting for driver...");
                                progressDialog.setCancelable(true);
                                progressDialog.show();
                                waitForFieldValueUpdate("served",1,progressDialog);
                            }else {
                                Toast.makeText(user_1.this, "Ambulance is on the way!!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

            }
        });
    }

public void endd()
{

    Intent obj = new Intent(getApplicationContext(),user_maps.class);
    Log.e("resserved", String.valueOf(res.served));
    obj.putExtra("res", res.user);
    startActivity(obj);
    finish();
}
    public void func(String user)
    {
        ProgressDialog pd = new ProgressDialog(user_1.this);
        pd.setTitle("Loading");
        pd.setMessage("Finding a driver. Please wait!!");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

            Log.e("Latitude:" , loc[1]);
            Log.e("Longitude:" , loc[0]);
            user_req_obj req1=new user_req_obj(user,loc[1],loc[0]);
            wdatabase= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
            wdatabase.child("try1").child("user_reqs").child(user).setValue(req1);

            if(wdatabase.child("try1").child("user_req").child(user).child("driver").get().toString()!="NAa")
            {
                Log.e("load","naa");
                pd.dismiss();
            }
    }

    public void getloc()
    {
        locationTrack = new LocationTrack(user_1.this);
        if (locationTrack.canGetLocation()) {
            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();
            try {
                if(longitude==0.0)
                {
                    Thread.sleep(4000);
                    locfetched=0;
                    longitude = locationTrack.getLongitude();
                    latitude = locationTrack.getLatitude();
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
        }
        else {
            locfetched=0;
            locationTrack.showSettingsAlert();
        }
    }
    private void waitForFieldValueUpdate(final String field, final int desiredValue,ProgressDialog progressDialog) {
        handler = new Handler();
        toggleChecker = new Runnable() {
            @Override
            public void run() {
                Log.e("waitupdate","Inside waitfor change loop");
                wdatabase1= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

                wdatabase1.child("user_reqs").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.getResult()==null)
                        {
                            return;
                        }
                        Log.e("waitupdate","Inside datachange");
                        user_req_obj check=task.getResult().getValue(user_req_obj.class);
                        int value=0;
                        if(check!=null)
                        {
                            value =check.getServed();
                        }
                        else{
                            Toast.makeText(user_1.this, "NULLLLL", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("value", String.valueOf(value));

                        if (value != 0 && value==desiredValue) {
                            // Desired value is updated in the database field
                            progressDialog.dismiss();
                            loopend=1;
                            Toast.makeText(user_1.this, "Ambulance is on the way!!", Toast.LENGTH_SHORT).show();
                            endd();
                        }
                        else {
                            loopend=0;
                        }
                    }
                });
                handler.postDelayed(this, 20000);
            }
        };
        Log.e("out","loop");
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
        new AlertDialog.Builder(user_1.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
//        handler.postDelayed(toggleChecker, 10000); // Start the initial check after 10 seconds
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(toggleChecker); // Stop the continuous checking
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Add the value event listener to start listening for changes
//        wdatabase1.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Remove the value event listener to stop listening for changes
//        wdatabase1.removeEventListener(valueEventListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}