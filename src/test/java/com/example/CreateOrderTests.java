package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(Parameterized.class)
public class CreateOrderTests {
    private String[] color;
    Order order;
    OrderClient orderClient;
    int track;

    public CreateOrderTests(String[] color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] testDataForColorOfScooter(){
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    @Before
    public void SetUp(){
        order = OrderGenerator.getDefaultBesidesColor(color);
        orderClient = new OrderClient();
    }

    @After
    public void TearDown(){
        orderClient.cancel(track);
    }

    @Test
    @DisplayName("Creating an order with all parameters and different colors")
    @Description("Test for create order with defoult parameters besides color. There are some parameters for test (color will be BLACK, GRAY, BLACK and GRAY, and no color choosing)")
    public void createOrderWithDifferentColorSettingsTest(){
        ValidatableResponse createOrderResponse = orderClient.create(order);
        int createOrderStatusCod = createOrderResponse.extract().statusCode();
        assertEquals("status cod is incorrect", SC_CREATED, createOrderStatusCod);
        track = createOrderResponse.extract().path("track");
        assertTrue(track > 0);
    }




}
