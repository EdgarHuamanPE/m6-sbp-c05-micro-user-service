package com.tecsup.app.micro.user.dto;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class User {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;

}
