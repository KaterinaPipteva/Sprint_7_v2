package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListOrdersTest {
    OrderClient orderClient;


    @Before
    public void SetUp(){
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("display list with orders via api/v1/orders")
    @Description("Test for try to list orders and check it status code and content")
    public void listOrdersSuccessTest(){
        ValidatableResponse listResponse = orderClient.list();
        int listStatusCode = listResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, listStatusCode);

        ArrayList<String> orders = listResponse.extract().path("orders");
        assertTrue("Orders are empty", orders != null);
    }
}
