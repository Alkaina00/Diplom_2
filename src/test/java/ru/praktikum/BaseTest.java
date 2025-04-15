package ru.praktikum;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.praktikum.model.Order;
import ru.praktikum.model.User;
import ru.praktikum.steps.OrderSteps;
import ru.praktikum.steps.UserSteps;

public class BaseTest {
    public final UserSteps userSteps = new UserSteps();
    public final OrderSteps orderSteps = new OrderSteps();
    public User user;
    public Order order;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter());

        order = new Order();
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@mail.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
    }

    @After
    public void tearDown() {
        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        if (token != null) {
            user.setToken(token);
            userSteps.deleteUser(user);
        }
    }
}
