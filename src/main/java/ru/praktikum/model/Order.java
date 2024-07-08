package ru.praktikum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
    private String[] ingredients;
    private String token;
}
