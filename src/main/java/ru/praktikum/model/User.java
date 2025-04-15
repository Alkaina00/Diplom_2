package ru.praktikum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String email;
    private String password;
    private String name;
    private String token;
}
