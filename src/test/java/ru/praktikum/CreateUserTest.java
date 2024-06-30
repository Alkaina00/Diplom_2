package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import ru.praktikum.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.steps.UserSteps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateUserTest {
    private final UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp(){
        RestAssured.filters(new RequestLoggingFilter());

        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10)+"@mail.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
    }

    @Test
    @DisplayName("Успешная проверка /api/v1/courier")
    @Description("Успешная проверка создания пользователя")
    public void testCreateUserOk(){
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
    public void testLoginUser(){
        userSteps.createUser(user);

        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", is("User already exists"));
    }

    @After
    public void tearDown(){
        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        if(token != null){
            user.setToken(token);
            userSteps.deleteUser(user);
        }
    }
}
