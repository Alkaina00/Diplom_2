package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest extends BaseTest {
    @Test
    @DisplayName("Успешная проверка /api/v1/courier")
    @Description("Успешная проверка создания пользователя")
    public void testCreateUserOk() {
        userSteps
                .createUser(user)
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Проверка ошибки на дубль пользователя /api/v1/courier")
    @Description("Проверка создания пользователя, который уже зарегистрирован")
    public void testLoginUser() {
        userSteps.createUser(user);

        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", is("User already exists"));
    }
}
