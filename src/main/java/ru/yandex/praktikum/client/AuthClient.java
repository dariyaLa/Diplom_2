package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.model.User;
import ru.yandex.praktikum.model.UserCredentials;

import static io.restassured.RestAssured.given;

public class AuthClient extends RestClient {

    public final String REGISTER_USER_PATH = BASE_URL + "/auth/register";
    public final String AUTH_USER_PATH = BASE_URL + "/auth/login";
    public final String GET_INFO_USER_PATH = BASE_URL + "/auth/user";

    @Step("Register client {client}")
    public Response register(User user) {
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .body(user)
                .when()
                .post(REGISTER_USER_PATH);
        return response;
    }

    @Step("Auth user {userCredentials}")
    public Response auth(UserCredentials userCredentials) {
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .body(userCredentials)
                .when()
                .post(AUTH_USER_PATH);
        return response;
    }

    @Step("Get info user")
    public Response getInfoUser(String token) {
        Response response = given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .log().all()
                .get(GET_INFO_USER_PATH);
        return response;
    }

    @Step("Changes info user {changesUser}")
    public Response change(User changesUser, String token) {
        Response response = given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .log().all()
                .body(changesUser)
                .when()
                .patch(GET_INFO_USER_PATH);
        return response;
    }

    @Step("Changes info user without auth {changesUser}")
    public Response changeWithoutAuth(User changesUser) {
        Response response = given()
                .spec(getBaseSpec())
                .log().all()
                .body(changesUser)
                .when()
                .patch(GET_INFO_USER_PATH);
        return response;
    }

    @Step("Delete user")
    public Response deleteUser(String token) {
        Response response = given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .log().all()
                .delete(GET_INFO_USER_PATH);
        return response;
    }
}
