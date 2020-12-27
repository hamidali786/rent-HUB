package com.example.rent_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.rent_hub.Model.Users;
import com.example.rent_hub.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {
    public EditText email, phone, password;
    public CheckBox remember_me_cb;
    public EditText forgot_password;
    public Button login_btn;
    public ProgressDialog loadingBar;
    public EditText admin_panel_link, not_admin_panel_link;
    public String parentDBName ="Users";
    private DataSnapshot dataSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        email = findViewById( R.id.email);
        phone = findViewById( R.id.phone );
        password = findViewById( R.id.password );
        loadingBar= new ProgressDialog( this );
        remember_me_cb = findViewById( R.id.remember_me_cb );
        //Paper dependency from Paper github for remember me check box to stored email once click
        Paper.init( this );
        login_btn = findViewById( R.id.login_btn );
        login_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(); //create login user method
            }
        } );

    }

    private void loginUser() {

        String mail = email.getText().toString();
        String phoneNo = phone.getText().toString();
        String pass = password.getText().toString();

        //apply else if condition on mail if empty
        if (TextUtils.isEmpty( mail )){
            Toast.makeText(this, "Please Enter Correct E-mail Address.",
                    Toast.LENGTH_LONG).show();
        }
        //apply else if condition on phone no if empty
        else if (TextUtils.isEmpty( phoneNo )){
            Toast.makeText(this, "Please Enter Correct Phone No.",
                    Toast.LENGTH_LONG).show();
        }
        //apply else if condition on password if empty
        else if (TextUtils.isEmpty( pass )){
            Toast.makeText(this, "Please Enter Correct Password.",
                    Toast.LENGTH_LONG).show();
        }
        else{
            loadingBar.setTitle( "Login Account..." ); // loading title when user click on create account for waiting
            loadingBar.setMessage("Please wait, While Checking Credential..."); //loading message when user click on create account for waiting
            loadingBar.setCanceledOnTouchOutside( false ); //if user click on screen then dialogue will not disappear
            loadingBar.show();
            AllAccessToAccount(mail, phoneNo, password);
        }
    }

    private void AllAccessToAccount(final String mail, final String phoneNo, final EditText password) {
        //If condition on check box from Paper library in Prevalent.java
        if (remember_me_cb.isChecked()) {
            //whenever user login it will pass the phone and pass and will be store
            Paper.book().write( Prevalent.UserPhoneKey, phoneNo );
            Paper.book().write( Prevalent.UserPasswordKey, password );
        }
        //create database reference
        //get reference using getInstance
        //RootRef is the object of database reference
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        //Check whether user data available or not
        RootRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dataSnapshot.child(parentDBName ).child(phoneNo).exists()){
                    Users usersData = dataSnapshot.child( parentDBName).child( phoneNo ).getValue(Users.class); //get phone number
                    //retrieve user data and applies check if exists in database then login
                    if (usersData.getPhoneNo().equals( phoneNo )){
                        if (usersData.getPassword().equals( password)){
                            if (usersData.getMail().equals( mail)){
                                Toast.makeText(loginActivity.this, "Logged In Successfully.", Toast.LENGTH_LONG ).show();
                                loadingBar.dismiss();
                                //move to home activity if user login successfully form login activity intent
                                Intent intent = new Intent( loginActivity.this, homeActivity.class );
                                startActivity( intent );
                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText( loginActivity.this, "Password you Entered is invalide.", Toast.LENGTH_LONG ).show();

                            }
                        }
                    }
                }
                else {
                    Toast.makeText(loginActivity.this, "Account wit This " + phoneNo + " do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(loginActivity.this, "You need to create Account..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }

}