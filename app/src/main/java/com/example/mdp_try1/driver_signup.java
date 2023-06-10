package com.example.mdp_try1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class driver_signup extends AppCompatActivity {

    TextView login;
    EditText username,password, repassword,num,name,mail;
    Button signup,signin;
    driver_reg_obj h1;
    FirebaseDatabase database;
    DatabaseReference wdatabase;
    int maxid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);


        password = (EditText) findViewById(R.id.inputPassword);
        repassword = (EditText) findViewById(R.id.inputConformPassword);
        num=(EditText) findViewById(R.id.inputnumber);
        name=(EditText) findViewById(R.id.inputname);
        username=(EditText) findViewById(R.id.inputUsername);
        mail=(EditText) findViewById(R.id.inputEmail);
        signup=(Button)findViewById(R.id.btnRegister);
        wdatabase=FirebaseDatabase.getInstance("https://hosp-db-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String fullname = name.getText().toString();
                String mailid = mail.getText().toString();
                String mob = num.getText().toString();
                String repass = repassword.getText().toString();
                if(user.equals("")||pass.equals("")||repass.equals("")||mob.equals("")||fullname.equals("")||mailid.equals(""))
                    Toast.makeText(driver_signup.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        h1= new driver_reg_obj(user,fullname, mailid,pass,mob);
                        wdatabase.child("try1").child("drivers").child(user).setValue(h1);
                        Toast.makeText(driver_signup.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),driver_login.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(driver_signup.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                    }
                } }
        });

    }
    public void login(View view) {
        Intent intent = new Intent(driver_signup.this,driver_login.class);
        startActivity(intent);
    }
}