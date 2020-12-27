package com.example.rent_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rent_hub.Model.Users;
import com.example.rent_hub.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    Button main_signup_btn, main_login_btn = findViewById( R.id.main_login_btn );
    Button loginbtn;
    private DataSnapshot dataSnapshot;
    public ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        loadingBar = new ProgressDialog( this );
        main_signup_btn = findViewById( R.id.main_signup_btn );
        // initialize Paper library
        Paper.init( this );
        loginbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, loginActivity.class );
                startActivity(intent);
            }
        } );
        main_signup_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, RegisterActivity.class );
                startActivity( intent );
            }
        } );
        // Retrieve user data using Paper dependency library.
        String UserPhoneKey = Paper.book().read( Prevalent.UserPhoneKey );
        String UserPasswordKey = Paper.book().read( Prevalent.UserPasswordKey );
        if (UserPhoneKey != "" && UserPasswordKey != ""){
            if (!TextUtils.isEmpty( UserPasswordKey ) && !TextUtils.isEmpty( UserPhoneKey)){
                AllowAccess(UserPhoneKey, UserPasswordKey);
                loadingBar.setTitle( "Already Logged In..." ); // loading title when user click on create account for waiting
                loadingBar.setMessage("Please wait, While Checking Credential..."); //loading message when user click on create account for waiting
                loadingBar.setCanceledOnTouchOutside( false ); //if user click on screen then dialogue will not disappear
                loadingBar.show();
            }
        }
    }

    private void AllowAccess(String userPhoneKey, String userPasswordKey) {

    }

    private void AllowAccess(final String mail,final String phoneNo,final String password) {

        //create database reference
        //get reference using getInstance
        //RootRef is the object of database reference
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        //Check whether user data available or not
        RootRef.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dataSnapshot.child("Users" ).child(phoneNo).exists()){
                    Users usersData = dataSnapshot.child( "Users").child( phoneNo ).getValue(Users.class); //get phone number
                    //retrieve user data and applies check if exists in database then login
                    if (usersData.getPhoneNo().equals( phoneNo )){
                        if (usersData.getPassword().equals( password)){
                            if (usersData.getMail().equals( mail)){
                                Toast.makeText(MainActivity.this, "You'r are already logged In Please Wait...", Toast.LENGTH_LONG ).show();
                                loadingBar.dismiss();
                                //move to home activity if user login successfully form login activity intent
                                Intent intent = new Intent( MainActivity.this, homeActivity.class );
                                startActivity( intent );
                            }
                            else {
                                    loadingBar.dismiss();
                                Toast.makeText( MainActivity.this, "Password you Entered is Invalid.", Toast.LENGTH_LONG ).show();

                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Account wit This " + phoneNo + " do not exists.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "You need to create Account..", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );
    }
}