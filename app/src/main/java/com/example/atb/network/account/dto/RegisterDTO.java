package com.example.atb.network.account.dto;

import lombok.Data;

@Data
public class RegisterDTO {
    private String email;
    private String firstName;
    private String secondName;
    private String photo;
    private String phone;
    private String password;
    private String confirmPassword;
}
