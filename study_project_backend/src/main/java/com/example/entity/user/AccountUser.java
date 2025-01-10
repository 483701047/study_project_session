package com.example.entity.user;

import lombok.Data;

@Data
public class AccountUser {

    int id;
    String username;
    String password;
    String email;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
