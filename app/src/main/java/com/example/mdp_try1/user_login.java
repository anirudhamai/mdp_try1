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

public class user_login extends AppCompatActivity {

    boolean c;
    EditText username, password;
    Button btnlogin;
    TextView dis;
    user_obj res;
    DatabaseReference wdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnlogin = (Button) findViewById(R.id.loginbtn);
        dis=(TextView)findViewById(R.id.txtSignUp) ;

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String passw = password.getText().toString();

                if(user.equals("")||passw.equals(""))
                    Toast.makeText(user_login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = check(user,passw);
                    if(checkuserpass==true){
                        Toast.makeText(user_login.this, "Sign in successfull", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), driver_login.class);
                        intent.putExtra("uname",user);
                        startActivity(intent);
                    }else{
                        Toast.makeText(user_login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent obj = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(obj);
                finish();
            }
        });
    }

    public boolean check(String user, String passw)
    {
        wdatabase= FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        wdatabase.child("try1").child("app_users").child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    res= task.getResult().getValue(user_obj.class);
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
}