package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.Order;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.INGREDIENTS;
import static ru.praktikum.EndPoints.ORDER;

public class OrderSteps extends BaseClient {
    @Step("Создание заказа /api/orders")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .contentType(ContentType.JSON)
                .spec(requestSpec)
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Получение ингредиентов /api/ingredients")
    public ValidatableResponse getIngredient() {
        return given()
                .spec(requestSpec)
                .get(INGREDIENTS)
                .then();
    }

    @Step("Получение заказов конкретного пользователя /api/orders")
    public ValidatableResponse getOrder(Order order) {
        return given()
                .spec(requestSpec)
                .headers("Authorization", order.getToken())
                .get(ORDER)
                .then();
    }
}
