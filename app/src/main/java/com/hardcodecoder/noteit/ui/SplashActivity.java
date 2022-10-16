package com.hardcodecoder.noteit.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends NoteBaseActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(null == mAuth.getCurrentUser()){
            //User is not signed in
            startActivity(new Intent(this, SignInChooserActivity.class));
        }
        else{
            //User is already signed in
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}
