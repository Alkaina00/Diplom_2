import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {
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
    @DisplayName("Проверка авторизации пользователя /api/auth/login")
    @Description("Успешная проверка авторизации пользователя")
    public void testLoginUser(){
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
    public void testLoginUserEmailIncorrect(){
        userSteps.createUser(user);

        user.setPassword(RandomStringUtils.randomAlphabetic(10)+"incorrect");
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным логином /api/auth/login")
    @Description("Проверка авторизации с неверным логином")
    public void testLoginUserPasswordIncorrect(){
        userSteps.createUser(user);

        user.setEmail(RandomStringUtils.randomAlphabetic(10)+"@yandex.ru");
        userSteps
                .loginUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
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
