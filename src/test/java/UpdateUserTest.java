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

public class UpdateUserTest {
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
    @DisplayName("Успешная проверка /api/auth/user")
    @Description("Успешная проверка обновления email")
    public void testUpdateUserEmailOk(){
        userSteps.createUser(user);
        userSteps.loginUser(user);

        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        user.setToken(token);

        String email = "newEmail@mail.ru";
        user.setEmail(email);
        userSteps
                .updateUser(user)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", is(email.toLowerCase()));
    }

    @Test
    @DisplayName("Успешная проверка /api/auth/user")
    @Description("Успешная проверка обновления password")
    public void testUpdateUserPasswordOk(){
        userSteps.createUser(user);
        userSteps.loginUser(user);

        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        user.setToken(token);

        user.setPassword("newPassword");
        userSteps
                .updateUser(user)
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Успешная проверка /api/auth/user")
    @Description("Успешная проверка обновления name")
    public void testUpdateUserNameOk(){
        userSteps.createUser(user);
        userSteps.loginUser(user);

        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        user.setToken(token);

        user.setName("newName");
        userSteps
                .updateUser(user)
                .statusCode(200)
                .body("success", is(true))
                .body("user.name", is("newName"));
    }

    @Test
    @DisplayName("Успешная проверка /api/auth/user")
    @Description("Успешная проверка обновления всех полей")
    public void testUpdateUserOk(){
        userSteps.createUser(user);
        userSteps.loginUser(user);

        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        user.setToken(token);

        String email = "newEmail@mail.ru";
        user.setEmail(email);
        user.setPassword("newPassword");
        user.setName("newName");

        userSteps
                .updateUser(user)
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", is(email.toLowerCase()))
                .body("user.name", is("newName"));
    }

    @Test
    @DisplayName("Проверка обновления без авторизации /api/auth/user")
    @Description("Проверка обновления данных пользователя без авторизации")
    public void testUpdateUserUnauthorized(){
        userSteps.createUser(user);

        String email = "newEmail@mail.ru";
        user.setEmail(email);
        user.setPassword("newPassword");
        user.setName("newName");
        user.setToken("");

        userSteps
                .updateUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Проверка обновления email без авторизации /api/auth/user")
    @Description("Проверка обновления email пользователя без авторизации")
    public void testUpdateEmailUserUnauthorized(){
        userSteps.createUser(user);

        String email = "newEmail@mail.ru";
        user.setEmail(email);
        user.setToken("");

        userSteps
                .updateUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Проверка обновления пароля без авторизации /api/auth/user")
    @Description("Проверка обновления пароля пользователя без авторизации")
    public void testUpdatePasswordUserUnauthorized(){
        userSteps.createUser(user);

        user.setPassword("newPassword");
        user.setToken("");

        userSteps
                .updateUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Проверка обновления имени без авторизации /api/auth/user")
    @Description("Проверка обновления имени пользователя без авторизации")
    public void testUpdateNameUserUnauthorized(){
        userSteps.createUser(user);

        user.setName("newName");
        user.setToken("");

        userSteps
                .updateUser(user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
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
