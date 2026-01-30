package com.tecsup.app.micro.user.dto;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class User {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

}
