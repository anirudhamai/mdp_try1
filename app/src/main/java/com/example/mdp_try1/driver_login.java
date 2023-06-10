package com.example.mdp_try1;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class driver_login extends AppCompatActivity {

    boolean c;
    EditText username, password;
    Button btnlogin;
    TextView signup;
    driver_reg_obj res;
    DatabaseReference wdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.loginButton);
        signup=(TextView)findViewById(R.id.signupText);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gosignup(view);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String passw = password.getText().toString();

                if(user.equals("")||passw.equals(""))
                    Toast.makeText(driver_login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = check(user,passw);
                    if(checkuserpass==true){
                        Toast.makeText(driver_login.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), driver1.class);
                        intent.putExtra("uname",user);
                        startActivity(intent);
                    }else{
                        Toast.makeText(driver_login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public boolean check(String user, String passw)
    {
        wdatabase= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        wdatabase.child("try1").child("drivers").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    res= task.getResult().getValue(driver_reg_obj.class);
                    if(passw.equals(res.getPass())){
                        c=true;
                    }else {
                        c=false;
                    }
                }
            }
        });
        return c;
    }

    public void gosignup(View view) {
        Intent obj = new Intent(getApplicationContext(),driver_signup.class);
        startActivity(obj);
    }
}