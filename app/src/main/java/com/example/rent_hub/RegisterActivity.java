package com.example.rent_hub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    public Button create_account_btn;
    public EditText register_email, register_username, register_phone, register_user_address, register_password;
    public ProgressDialog loadingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        create_account_btn = findViewById( R.id.create_account_btn );
        register_username = findViewById( R.id.register_username );
        register_email = findViewById( R.id.register_email);
        register_phone = findViewById( R.id.register_phone );
        register_user_address = findViewById( R.id.register_user_address );
        register_password = findViewById( R.id.register_password );
        loadingBar= new ProgressDialog( this );
        //on click listener on create account button
        create_account_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        } );
    }
        //create account with parameters covert to string first
        public void CreateAccount(){
            String username = register_username.getText().toString();
            String mail = register_email.getText().toString();
            String phoneNo = register_phone.getText().toString();
            String userAddress = register_user_address.getText().toString();
            String password = register_password.getText().toString();
            //apply if condition on username if empty
            if (TextUtils.isEmpty( username )){
                Toast.makeText(this, "Please Enter Correct username",
                        Toast.LENGTH_LONG).show();
            }
            //apply else if condition on mail if empty
            else if (TextUtils.isEmpty( mail )){
                Toast.makeText(this, "Please Enter Correct E-mail Address.",
                        Toast.LENGTH_LONG).show();
            }
            //apply else if condition on phone no if empty
            else if (TextUtils.isEmpty( phoneNo )){
                Toast.makeText(this, "Please Enter Correct Phone No.",
                        Toast.LENGTH_LONG).show();
            }
            //apply else if condition on user address if empty
            else if (TextUtils.isEmpty( userAddress )){
                Toast.makeText(this, "Please Enter Correct Address.",
                        Toast.LENGTH_LONG).show();
            }
            //apply else if condition on password if empty
            else if (TextUtils.isEmpty( password )){
                Toast.makeText(this, "Please Enter Correct Password.",
                        Toast.LENGTH_LONG).show();
            }
            //apply else condition when user click on create account button with loading bar
            else{
                loadingBar.setTitle( "Creating Account..." ); // loading title when user click on create account for waiting
                loadingBar.setMessage("Please wail, While Checking Credential..."); //loading message when user click on create account for waiting
                loadingBar.setCanceledOnTouchOutside( false ); //if user click on screen then dialogue will not disappear
                loadingBar.show();
                // now check validation
                ValidatephoneNumber(mail, username, phoneNo, userAddress, password);
            }
        }
            //check phone number of the user in the database
    private void ValidatephoneNumber(final String username,
                                     final String mail,
                                     final String phoneNo,
                                     final String userAddress,
                                     final String password) {
        //create database reference
        final DatabaseReference RootRef; //RootRef is the object of database reference
        RootRef = FirebaseDatabase.getInstance().getReference(); //get reference using getInstance
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phoneNo).exists())) {       //if phone not exist then create another account
                    HashMap<String, Object> userdataMap = new HashMap<>(); //get data using hash map

                    userdataMap.put("name", username);
                    userdataMap.put("mail", mail);
                    userdataMap.put("phone", phoneNo);
                    userdataMap.put("userAddress", userAddress);
                    userdataMap.put("password", password);
                    //inside root ref show phone number in user child
                    RootRef.child("Users").child(phoneNo).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override public void onComplete(@NonNull Task<Void> task) {
                           //if task complete give user a Congratulations message in toast
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Congratulations, your account has been created.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                //once account create then move user to login activity
                                Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
                                startActivity(intent);
                                //else message when some error occurs like network error
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                            } } });
                } else {
                    Toast.makeText(RegisterActivity.this, "This " + phoneNo + " already exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            } });
    }

}