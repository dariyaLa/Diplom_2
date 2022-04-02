package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends RestClient {

    public final String GET_INGREDIENTS_PATH = BASE_URL + "/ingredients";

    @Step("Get ingredients")
    public Response getIngredients() {
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .get(GET_INGREDIENTS_PATH);
        return response;
    }

    @Step("Get ingredients")
    public Response getIngredientsWithAuthToken(String token) {
        Response response = given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .log().all()
                .get(GET_INGREDIENTS_PATH);
        return response;
    }
}
