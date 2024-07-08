package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.User;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.*;

public class UserSteps extends BaseClient {
    @Step("Создание пользователя /api/auth/register")
    public ValidatableResponse createUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .spec(requestSpec)
                .body(user)
                .when()
                .post(REGISTER)
                .then();
    }

    @Step("Авторизация пользователя /api/auth/login")
    public ValidatableResponse loginUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .spec(requestSpec)
                .body(user)
                .when()
                .post(LOGIN)
                .then();
    }

    @Step("Удаление пользователя /api/auth/user")
    public ValidatableResponse deleteUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .headers("Authorization", user.getToken())
                .spec(requestSpec)
                .when()
                .delete(USER)
                .then();
    }

    @Step("Обновление пользователя /api/auth/user")
    public ValidatableResponse updateUser(User user) {
        return given()
                .contentType(ContentType.JSON)
                .headers("Authorization", user.getToken())
                .spec(requestSpec)
                .body(user)
                .when()
                .patch(USER)
                .then();
    }
}
