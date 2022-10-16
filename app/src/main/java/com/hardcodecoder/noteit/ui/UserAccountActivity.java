package com.hardcodecoder.noteit.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hardcodecoder.noteit.BuildConfig;
import com.hardcodecoder.noteit.R;
import com.hardcodecoder.noteit.account.CredentialsStandardVerifier;
import com.hardcodecoder.noteit.account.UserInfo;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserAccountActivity extends NoteBaseActivity {

    private static final int PICK_AVATAR = 100;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String name, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        findViewById(R.id.close_btn).setOnClickListener(v -> finish());
        ImageView userImage = findViewById(R.id.user_img);
        Bitmap bitmap = UserInfo.getUserProfileImage(getFilesDir().getAbsolutePath());
        if (null != bitmap) userImage.setImageBitmap(bitmap);
        else userImage.setImageResource(R.drawable.ic_user);

        userImage.setOnClickListener(v -> updateProfilePicture());
        findViewById(R.id.user_email_edit_btn).setOnClickListener(v -> updateUserInfo());
        findViewById(R.id.log_out).setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, getString(R.string.toast_signed_out), Toast.LENGTH_LONG).show();
        });
        TextView footer = findViewById(R.id.footer);
        footer.setText(getString(R.string.footer).concat(BuildConfig.VERSION_NAME));
        updateUserProfileUi();
    }

    private void updateUserProfileUi() {
        if (null != mUser) {
            name = mUser.getDisplayName();
            email = mUser.getEmail();
            String nameAndEmail = String.format("%s %s\n%s %s", getString(R.string.user_name), name, getString(R.string.user_email_id), email);
            TextView temp = findViewById(R.id.user_email);
            temp.setText(nameAndEmail);
            if (mUser.getMetadata() != null) {
                long lastSignedIn = mUser.getMetadata().getLastSignInTimestamp();
                DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
                Calendar calender = Calendar.getInstance();
                calender.setTimeInMillis(lastSignedIn);
                String lastSignedInText = getString(R.string.last_sign_in) + dateFormat.format(calender.getTime());
                temp = findViewById(R.id.last_synced);
                temp.setText(lastSignedInText);
            }
        }
    }

    private void updateProfilePicture() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_profile_image));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_AVATAR);
    }

    private void updateUserInfo() {
        View dialogView = View.inflate(this, R.layout.update_user_info_layout, null);
        RoundedBottomSheetDialog bottomSheetDialog = new RoundedBottomSheetDialog(this);
        bottomSheetDialog.setContentView(dialogView);

        TextInputLayout tilName = dialogView.findViewById(R.id.update_name);
        TextInputLayout tilEmail = dialogView.findViewById(R.id.update_email);

        TextInputEditText editName = dialogView.findViewById(R.id.name_et);
        TextInputEditText editEmail = dialogView.findViewById(R.id.email_et);

        editName.setText(name);
        editEmail.setText(email);


        dialogView.findViewById(R.id.cancel_btn).setOnClickListener(v -> bottomSheetDialog.dismiss());
        dialogView.findViewById(R.id.confirm_btn).setOnClickListener(v -> {
            if (null == editName.getText()) tilName.setError(getString(R.string.user_name_error));
            else if (null == editEmail.getText())
                tilEmail.setError(getString(R.string.user_email_id_error));
            else {
                String name = editName.getText().toString().trim();
                String email = editEmail.getText().toString();
                if (name.length() <= 0) tilName.setError(getString(R.string.user_name_error));
                else if (CredentialsStandardVerifier.isValidEmailFormat(email)) {
                    tilEmail.setError(null);
                    tilName.setError(null); //Reset error messages
                    updateAccountWith(name, email);
                    bottomSheetDialog.dismiss();
                } else {
                    tilEmail.setError(getString(R.string.user_email_invalid));
                }
            }
        });
        bottomSheetDialog.show();
    }

    private void updateAccountWith(String name, String email) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (null != user) {
            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
            user.updateProfile(changeRequest);
            user.updateEmail(email).addOnSuccessListener(task -> {
                Toast.makeText(this, getString(R.string.toast_email_id_updated), Toast.LENGTH_SHORT).show();
                updateUserProfileUi();
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == PICK_AVATAR) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                String userDir = UserInfo.getUserStorageDir();
                StorageReference storageReference = storage.getReference(userDir + "profile_picture");
                if (null != data.getData()) {
                    ImageView profile = findViewById(R.id.user_img);
                    profile.setImageDrawable(null);
                    ProgressBar progressBar = findViewById(R.id.progress_bar_circular);
                    progressBar.setVisibility(View.VISIBLE);
                    storageReference.putFile(data.getData()).addOnSuccessListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(),
                                    data.getData());
                            profile.setImageBitmap(bitmap);

                            UserInfo.saveUserProfileImage(bitmap, getFilesDir().getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).addOnFailureListener(task -> {
                        Log.w("TAG", "upload Failed");
                        Toast.makeText(this, getString(R.string.toast_user_profile_photo_upload_failed), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }
    }
}
