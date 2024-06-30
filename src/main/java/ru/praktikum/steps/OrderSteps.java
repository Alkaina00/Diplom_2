package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.Order;

import static ru.praktikum.EndPoints.*;
import static ru.praktikum.config.RestConfig.HOST;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Создание заказа /api/orders")
    public ValidatableResponse createOrder(Order order){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(HOST)
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Получение ингредиентов /api/ingredients")
    public ValidatableResponse getIngredient(){
        return given()
                .baseUri(HOST)
                .get(INGREDIENTS)
                .then();
    }

    @Step("Получение заказов конкретного пользователя /api/orders")
    public ValidatableResponse getOrder(Order order){
        return given()
                .baseUri(HOST)
                .headers("Authorization", order.getToken())
                .get(ORDER)
                .then();
    }
}
