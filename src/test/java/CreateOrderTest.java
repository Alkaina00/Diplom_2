import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import model.Order;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateOrderTest {
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
    @DisplayName("Успешная проверка с авторизацией /api/orders")
    @Description("Успешная проверка создания заказа авторизованным пользователем")
    public void testCreateOrderAuth(){
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

        orderSteps
                .createOrder(order)
                .statusCode(200)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Успешная проверка без авторизации /api/orders")
    @Description("Успешная проверка создания заказа неавторизованным пользователем")
    public void testCreateOrder(){
        List<String> ids_ingredients = orderSteps.getIngredient()
                .extract().body().path("data._id");
        Object[] arrayIngredients = ids_ingredients.toArray();
        String[] ingredients = Arrays.copyOf(arrayIngredients, arrayIngredients.length, String[].class);
        order.setIngredients(ingredients);

        orderSteps
                .createOrder(order)
                .statusCode(200)
                .body("success", is(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Проверка без ингредиентов /api/orders")
    @Description("Проверка создания заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients(){
        userSteps.createUser(user);
        userSteps.loginUser(user);

        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        order.setToken(token);

        orderSteps
                .createOrder(order)
                .statusCode(400)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Проверка с неверным хешем ингредиентов /api/orders")
    @Description("Проверка создания заказа с неверным хешем ингредиентов")
    public void testCreateOrderIncorrectIngredient(){
        userSteps.createUser(user);
        userSteps.loginUser(user);

        String token = userSteps.loginUser(user)
                .extract().body().path("accessToken");
        order.setToken(token);

        String[] ingredients = {"60d3b41abdacab0026a733incorrectc6"};
        order.setIngredients(ingredients);

        orderSteps
                .createOrder(order)
                .statusCode(500);
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
