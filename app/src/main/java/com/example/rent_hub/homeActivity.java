package com.example.rent_hub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class homeActivity extends AppCompatActivity {
    Button logOutbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        logOutbtn = findViewById( R.id.logOutbtn );
        logOutbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete all the values form the user account
                Paper.book().destroy();
                Intent intent = new Intent( homeActivity.this, MainActivity.class );
                startActivity( intent );
            }
        } );
    }
}