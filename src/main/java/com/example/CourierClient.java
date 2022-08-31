package com.example;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {
    private static final String COURIER_CREATE_PATH = "api/v1/courier";
    private static final String COURIER_LOGIN_PATH = "api/v1/courier/login";
    private static final String COURIER_DELETE_PATH = "api/v1/courier/";

    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_CREATE_PATH)
                .then();
    }

    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .log().all()
                .body(courierCredentials)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then();
    }

    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseSpec())
                .pathParam("id", id)
                .log().all()
                .when()
                .delete(COURIER_DELETE_PATH+ "{id}")
                .then()
                .log().all();
    }

}
