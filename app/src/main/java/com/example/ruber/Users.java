package com.example.ruber;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    public String email = "", password = "";

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
