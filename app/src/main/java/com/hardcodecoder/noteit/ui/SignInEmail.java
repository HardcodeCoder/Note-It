package com.hardcodecoder.noteit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.account.CredentialsStandardVerifier;

public class SignInEmail extends NoteBaseActivity {

    //private static final String TAG = "SignInEmailActivity";
    private FirebaseAuth mAuth;
    private TextInputEditText mEditTextEmail;
    private String email;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_email);

        mAuth = FirebaseAuth.getInstance();
        mEditTextEmail = findViewById(R.id.email_et);
        mProgressBar = findViewById(R.id.pb);
        findViewById(R.id.sign_in_btn).setOnClickListener(v -> verifyEmailId());
    }

    private void verifyEmailId(){
        showProgressBar();
        if(null != mEditTextEmail.getText()) {
            email = mEditTextEmail.getText().toString();
            TextInputLayout til = findViewById(R.id.email_layout);
            if(!CredentialsStandardVerifier.isValidEmailFormat(email)){
                til.setError(getString(R.string.email_error));
                hideProgressBar();
                return;
            }
            til.setError(null);
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(SignInEmail.this, task -> {
                hideProgressBar();
                if(task.getResult() != null && task.getResult().getSignInMethods() != null) {
                    if (task.getResult().getSignInMethods().size() == 1) {
                        //Call sign in method
                        signInUser(email);
                    } else {
                        //User is not registered with this email
                        //Call sign up method;
                        signUpWithEmail(email);
                    }
                }
            });
        }
    }

    private void signUpWithEmail(final String emailId) {
        TextView tv = findViewById(R.id.welcome_back);
        tv.setText(getString(R.string.greetings_new_user));

        findViewById(R.id.display_name_layout).setVisibility(View.VISIBLE);

        TextInputLayout til = findViewById(R.id.password_layout);
        til.setVisibility(View.VISIBLE);

        TextInputEditText et = findViewById(R.id.password_et);
        TextInputEditText et2 = findViewById(R.id.name_et);

        Button btn = findViewById(R.id.sign_in_btn);
        btn.setText(getString(R.string.sign_up_btn));

        btn.setOnClickListener(v -> {
            if(null != mEditTextEmail.getText() && !emailId.equals(mEditTextEmail.getText().toString())){
                // User changed email after entering password
                // re-validate new email and cancel current sign up
                til.setVisibility(View.GONE);
                if(null != et.getText())
                    et.getText().clear();
                verifyEmailId();
                return;
            }

            til.setError(null);
            showProgressBar();
            String password = "";
            if (null != et.getText())
                password = et.getText().toString();

            if (!CredentialsStandardVerifier.isValidPasswordFormat(password)) {
                til.setError(getString(R.string.password_error));
                hideProgressBar();
                return;
            }
            mAuth.createUserWithEmailAndPassword(emailId, password)
                    .addOnCompleteListener(SignInEmail.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (null != user) {
                                String displayName = "";
                                if (null != et2.getText()) displayName = et2.getText().toString();
                                UserProfileChangeRequest upcr = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(displayName)
                                        .build();
                                user.updateProfile(upcr).addOnCompleteListener(SignInEmail.this, task2 -> updateUi());
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInEmail.this, "Authentication failed. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }

    private void signInUser(final String emailId) {
        TextView tv = findViewById(R.id.welcome_back);
        tv.setText(getString(R.string.welcome_back));
        TextInputLayout til = findViewById(R.id.password_layout);
        til.setVisibility(View.VISIBLE);
        TextInputEditText et = findViewById(R.id.password_et);

        Button btn = findViewById(R.id.sign_in_btn);
        btn.setText(getString(R.string.sign_in_btn));

        btn.setOnClickListener(v -> {

            if(null != mEditTextEmail.getText() && !emailId.equals(mEditTextEmail.getText().toString())){
                // User changed email after entering password
                // re-validate new email and cancel current sign in
                til.setVisibility(View.GONE);
                if(null != et.getText())
                    et.getText().clear();
                verifyEmailId();
                return;
            }

            til.setError(null);
            showProgressBar();
            String password = "";
            if (null != et.getText())
                password = et.getText().toString();

            if(!CredentialsStandardVerifier.isValidPasswordFormat(password)){
                til.setError(getString(R.string.wrong_password));
                hideProgressBar();
                return;
            }
            mAuth.signInWithEmailAndPassword(emailId, password)
                    .addOnCompleteListener(SignInEmail.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            updateUi();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInEmail.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                til.setError(getString(R.string.wrong_password));
                                hideProgressBar();
                            }
                        }
                    });
        });
    }

    private void updateUi(){
        setResult(RESULT_OK,  new Intent());
        finish();
    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.INVISIBLE);
    }

}
