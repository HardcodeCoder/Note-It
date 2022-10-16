package com.hardcodecoder.noteit.account;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserInfo {

    private static final String INVALID_USER = "NoUser";
    private static final String USER_PROFILE_IMAGE = "profile_picture";

    private static String getSignedInUserUid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(null != auth.getCurrentUser())
            return auth.getCurrentUser().getUid();
        return INVALID_USER;
    }

    public static String getUserStorageDir(){
        return "/users/" + getSignedInUserUid() + "/";
    }


    public static void saveUserProfileImage (Bitmap bitmap, String filesDir) {
        File f = new File(filesDir, USER_PROFILE_IMAGE);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != fos){
                try {
                    fos.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    public static Bitmap getUserProfileImage (String filesDir){
        try {
            File f = new File(filesDir, USER_PROFILE_IMAGE);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
