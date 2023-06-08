package com.example.mdp_try1;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class user_1 extends AppCompatActivity {

    Button getloc,book;
    int locfetched=0;
    DatabaseReference wdatabase;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    final String[] loc = new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user1);

        String user=getIntent().getStringExtra("uname").toString();

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
                locationTrack = new LocationTrack(user_1.this);
                if (locationTrack.canGetLocation()) {
                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();
                    loc[0] = String.valueOf(longitude);
                    loc[1] = String.valueOf(latitude);
                    locfetched=1;
                    Log.e("lon",loc[0]);
                    Log.e("lat",loc[1]);
                }
                else {
                    locfetched=0;
                    locationTrack.showSettingsAlert();
                }
            }
        });
        book=(Button)findViewById(R.id.button2);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locfetched==1)
                {

                    wdatabase= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                    wdatabase.child("try1").child("user_reqs").child(user).child("lon").setValue(loc[0]);
                    wdatabase.child("try1").child("user_reqs").child(user).child("lat").setValue(loc[1]);
                    ProgressDialog pd = new ProgressDialog(user_1.this);
                    pd.setTitle("Loading");
                    pd.setMessage("Finding a driver. Please wait!!");
                    pd.setCancelable(true);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();
                    if(wdatabase.child("try1").child("app_users").child(user).get()!=null)
                    {
                        pd.dismiss();
                    }
                }
                else {
                    new AlertDialog.Builder(user_1.this)
                            .setMessage("Please fetch your device location")
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }

}