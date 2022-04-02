package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class OrdersClient extends RestClient {

    public final String CREATE_ORDER_PATH = BASE_URL + "/orders";

    @Step("Create order {order}")
    public Response createOrder(Order order) {
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH);
        return response;
    }

    @Step("Create order {order}")
    public Response createOrderAuthUser(Order order, String token) {
        Response response = given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .log().all()
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH);
        return response;
    }

    @Step("Get orders {order}")
    public Response getOrdersUser(String token) {
        Response response = given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .log().all()
                .get(CREATE_ORDER_PATH);
        return response;
    }
}
