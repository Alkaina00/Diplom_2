package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest extends BaseTest {
    @Test
    @DisplayName("Проверка авторизации пользователя /api/auth/login")
    @Description("Успешная проверка авторизации пользователя")
    public void testLoginUser() {
        userSteps.createUser(user);

        userSteps
                .loginUser(user)
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем /api/auth/login")
    @Description("Проверка авторизации с неверным паролем")
    public void testLoginUserEmailIncorrect() {
        userSteps.createUser(user);

        user.setPassword(RandomStringUtils.randomAlphabetic(10) + "incorrect");
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным логином /api/auth/login")
    @Description("Проверка авторизации с неверным логином")
    public void testLoginUserPasswordIncorrect() {
        userSteps.createUser(user);

        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }
}
