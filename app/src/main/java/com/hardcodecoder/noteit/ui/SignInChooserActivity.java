package com.hardcodecoder.noteit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.utils.NetworkUtil;

public class SignInChooserActivity extends NoteBaseActivity {

    public static final short SIGN_SIGN_SUCCESS = 0;
    public static final String NEW_SIGN_IN = "NewSignIn";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in_chooser);

        /*findViewById(R.id.btn1).setOnClickListener(v-> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            signIn();
        });*/

        findViewById(R.id.btn2).setOnClickListener(v ->{
            if(NetworkUtil.isConnectedToInternet(this))
                startActivityForResult(new Intent(SignInChooserActivity.this, SignInEmail.class), SIGN_SIGN_SUCCESS);
            else showNetworkNotAvailableToast();
        });
        findViewById(R.id.skip_btn).setOnClickListener(v -> startActivity(new Intent(SignInChooserActivity.this, MainActivity.class)));
    }

    private void showNetworkNotAvailableToast(){
        Toast.makeText(this, "Connect to internet to sign in", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == SIGN_SIGN_SUCCESS) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(NEW_SIGN_IN, true);
                startActivity(intent);
                finish();

            }
           /* else if (requestCode == RC_SIGN_IN) {
                Log.e(TAG, "onActivityResult def id was = "+getString(R.string.default_web_client_id));
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if(account != null) {
                        Log.e(TAG, "account = "+account.getServerAuthCode());
                        Log.e(TAG, "Signed in account is not null");
                        firebaseAuthWithGoogle(account);
                    }
                } catch (ApiException e) {
                    Log.w(TAG, "Google sign in failed", e);
                }
            }*/
        }
    }

    /*private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Snackbar.make(findViewById(R.id.sign_in_chooser_root), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    }

                });
    }*/
}
