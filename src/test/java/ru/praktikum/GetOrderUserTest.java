package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderUserTest extends BaseTest {
    @Test
    @DisplayName("Успешное получение заказов /api/orders")
    @Description("Успешная проверка получения заказов авторизованным пользователем")
    public void testGetOrderOk() {
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
    public void testGetOrderUnauth() {
        order.setToken("");

        orderSteps.getOrder(order)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
