package com.giftyrani.loananalytics.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String role; // ROLE_ADMIN, ROLE_ANALYST, ROLE_VIEWER
}
