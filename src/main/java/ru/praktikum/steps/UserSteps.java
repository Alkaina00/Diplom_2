package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.User;

import static ru.praktikum.EndPoints.*;
import static ru.praktikum.config.RestConfig.HOST;
import static io.restassured.RestAssured.given;

public class UserSteps {
    @Step("Создание пользователя /api/auth/register")
    public ValidatableResponse createUser(User user){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOST)
                .body(user)
                .when()
                .post(REGISTER)
                .then();
    }

    @Step("Авторизация пользователя /api/auth/login")
    public ValidatableResponse loginUser(User user){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOST)
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Удаление пользователя /api/auth/user")
    public ValidatableResponse deleteUser(User user){
        return given()
                .contentType(ContentType.JSON)
                .headers("Authorization", user.getToken())
                .baseUri(HOST)
                .when()
                .delete(USER)
                .then();
    }

    @Step("Обновление пользователя /api/auth/user")
    public ValidatableResponse updateUser(User user){
        return given()
                .contentType(ContentType.JSON)
                .headers("Authorization", user.getToken())
                .baseUri(HOST)
                .body(user)
                .when()
                .patch(USER)
                .then();
    }
}
