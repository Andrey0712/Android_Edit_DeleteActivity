package com.example.atb.network.account.dto;

import lombok.Data;

@Data
public class RegisterErrorDTO {
    private String[] email;
    private String[] firstName;
    private String[] secondName;
    private String[] phone;
}
