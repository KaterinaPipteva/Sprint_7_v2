package com.example;

import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String CREATE_ORDER_PATH = "api/v1/orders";
    private static final String CANCEL_ORDER_PATH = "api/v1/orders/cancel";
    private static final String LIST_ORDERS_PATH = "api/v1/orders";


    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER_PATH)
                .then().log().all();
    }


    public ValidatableResponse cancel(int track) {
        return given()
                .spec(getBaseSpec())
                .pathParam("track", track)
                .log().all()
                .when()
                .put(CANCEL_ORDER_PATH+ "?track=" + "{track}")
                .then().log().all();
    }

    public ValidatableResponse list() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(LIST_ORDERS_PATH)
                .then();

    }
}
