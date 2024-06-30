package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import ru.praktikum.model.Order;
import ru.praktikum.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.steps.OrderSteps;
import ru.praktikum.steps.UserSteps;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderUser {
    private final OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private final UserSteps userSteps = new UserSteps();
    private User user;

    @Before
    public void setUp(){
        RestAssured.filters(new RequestLoggingFilter());

        order = new Order();
        user = new User();
        user.setEmail(RandomStringUtils.randomAlphabetic(10)+"@mail.ru");
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setName(RandomStringUtils.randomAlphabetic(10));
    }

    @Test
    @DisplayName("Успешное получение заказов /api/orders")
    @Description("Успешная проверка получения заказов авторизованным пользователем")
    public void testGetOrderOk(){
        userSteps.createUser(user);
        userSteps.loginUser(user);

        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        order.setToken(token);

        List<String> ids_ingredients = orderSteps.getIngredient()
                .extract().body().path("data._id");
        Object[] arrayIngredients = ids_ingredients.toArray();
        String[] ingredients = Arrays.copyOf(arrayIngredients, arrayIngredients.length, String[].class);
        order.setIngredients(ingredients);

        orderSteps.createOrder(order)
                .extract().body().path("order.number");

        orderSteps.getOrder(order)
                .statusCode(200)
                .body("orders.number", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованным пользователем /api/orders")
    @Description("Проверка получения заказов неавторизованным пользователем")
    public void testGetOrderUnauth(){
        order.setToken("");

        orderSteps.getOrder(order)
                .statusCode( 401)
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
