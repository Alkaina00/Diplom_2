package ru.praktikum.steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static ru.praktikum.config.RestConfig.HOST;

public class BaseClient {
    RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(HOST)
            .build();
}
