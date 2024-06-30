package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import ru.praktikum.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.steps.UserSteps;

import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class CreateUserRequiredFieldsTest {
    private final UserSteps userSteps = new UserSteps();
    private User user;
    private final String email;
    private final String password;
    private final String name;

    public CreateUserRequiredFieldsTest(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] data() {
        return new Object[][]{
                {null, "123pass", "tester"},
                {"Test@mail.ru", null, "tester"},
                {"Test@mail.ru", "123pass", null},
                {null, null, "tester"},
                {null, "123pass", null},
                {"Test@mail.ru", null, null},
                {null, null, null},
        };
    }

    @Before
    public void setUp(){
        RestAssured.filters(new RequestLoggingFilter());

        user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
    }

    @Test
    @DisplayName("Проверка ошибки обязательного поля /api/v1/courier")
    @Description("Проверка создания пользователя без обязательного поля")
    public void testCreateUserRequiredFieldsTest(){
        userSteps
                .createUser(user)
                .statusCode(403)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }
}
