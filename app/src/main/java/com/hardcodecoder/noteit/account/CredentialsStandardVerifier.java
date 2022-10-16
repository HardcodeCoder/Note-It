package com.hardcodecoder.noteit.account;

public class CredentialsStandardVerifier {

    private CredentialsStandardVerifier() {
    }

    public static boolean isValidEmailFormat(String email){
        email = email.trim();
        return (email.endsWith(".com") && email.contains("@"));
    }

    public static boolean isValidPasswordFormat(String password){
        password = password.trim();
        return (password.length()>=6);
    }
}
