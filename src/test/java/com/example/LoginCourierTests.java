package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.apache.http.HttpStatus.*;

public class LoginCourierTests {

    Courier courier;
    Courier courierRandom;
    CourierClient courierClient;
    int courierId;
    Courier courierWithDifferentLogin;
    Courier courierWithDifferentPass;
    Courier courierWithoutLogin;
    Courier courierWithoutPassword;

    @Before
    public void setUp(){
        courier = CourierGenerator.getDefault();
        courierRandom = CourierGenerator.getRandom();
        courierClient = new CourierClient();
        courierWithDifferentLogin = CourierGenerator.getWithDifferentLogin();
        courierWithDifferentPass = CourierGenerator.getWithDifferentPass();
        courierWithoutLogin = CourierGenerator.getWithoutLogin();
        courierWithoutPassword = CourierGenerator.getWithoutPassword();

    }

    @After
    public void tearDown(){
        courierClient.delete(courierId);
    }


    @Test
    @DisplayName("Logging new courier")
    @Description("Test for check login general courier")
    public void loginCourierSuccessTest(){
        ValidatableResponse createResponse = courierClient.create(courierRandom);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Courier didn't created", SC_CREATED, createStatusCode);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierRandom));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Courier didn't login", SC_OK, loginStatusCode);
        courierId = loginResponse.extract().path("id");
        assertTrue("id is incorrect", courierId > 0);
    }

    @Test
    @DisplayName("Login courier with non-existent login ")
    @Description("Failure test for checking login with false login parameter")
    public void loginWithFalseLoginTest(){
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Courier didn't created", SC_CREATED, createStatusCode);

        ValidatableResponse loginWithFalseLoginResponse = courierClient.login(CourierCredentials.from(courierWithDifferentLogin));
        int loginStatusCode = loginWithFalseLoginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_NOT_FOUND, loginStatusCode);
        String errorText = loginWithFalseLoginResponse.extract().path("message");
        assertEquals("Message is incorrect", ErrorTexts.getLoginErrorForFalseCredentials(), errorText);

        ValidatableResponse loginSuccessResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginSuccessResponse.extract().path("id");
    }

    @Test
    @DisplayName("Login courier with non-existent password ")
    @Description("Failure test for checking login with false password parameter")
    public void loginWithFalsePasswordTest(){
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Courier didn't created", SC_CREATED, createStatusCode);

        ValidatableResponse loginWithFalsePasswordResponse = courierClient.login(CourierCredentials.from(courierWithDifferentPass));
        int loginStatusCode = loginWithFalsePasswordResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_NOT_FOUND, loginStatusCode);
        String errorText = loginWithFalsePasswordResponse.extract().path("message");
        assertEquals("Message is incorrect", ErrorTexts.getLoginErrorForFalseCredentials(), errorText);

        ValidatableResponse loginSuccessResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginSuccessResponse.extract().path("id");
    }

    @Test
    @DisplayName("Login courier without login ")
    @Description("Failure test for checking login without login parameter")
    public void loginWithoutLoginFailureTest(){
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Courier didn't created", SC_CREATED, createStatusCode);

        ValidatableResponse loginWithoutLoginResponse = courierClient.login(CourierCredentials.from(courierWithoutLogin));
        int loginStatusCode = loginWithoutLoginResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, loginStatusCode);
        String errorText = loginWithoutLoginResponse.extract().path("message");
        assertEquals("Message is incorrect", ErrorTexts.getLoginErrorForAbsenceRequiredParameters(), errorText);

        ValidatableResponse loginSuccessResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginSuccessResponse.extract().path("id");
    }

    @Test
    @DisplayName("Login courier without password ")
    @Description("Failure test for checking login without password parameter")
    public void loginWithoutPasswordFailureTest(){
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Courier didn't created", SC_CREATED, createStatusCode);

        ValidatableResponse loginWithoutPassResponse = courierClient.login(CourierCredentials.from(courierWithoutPassword));
        int loginStatusCode = loginWithoutPassResponse.extract().statusCode();
        //на этом моменте тест падает потому что не отправляется "password" и ответ приходит 504 (считаю это багом)
        //при этом когда уходит запрос без "login" - все ок, возвращается ошибка
        try{
            assertEquals("Status code is incorrect", SC_BAD_REQUEST, loginStatusCode);
            String errorText = loginWithoutPassResponse.extract().path("message");
            assertEquals("Message is incorrect", ErrorTexts.getLoginErrorForAbsenceRequiredParameters(), errorText);}

        finally {
            ValidatableResponse loginSuccessResponse = courierClient.login(CourierCredentials.from(courier));
            courierId = loginSuccessResponse.extract().path("id");
        }
    }

    @Test
    @DisplayName("Login courier with non-existent parameters ")
    @Description("Failure test for checking login with credentials of deleted courier")
    public void loginWithNonExistingCourierFailureTest(){
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Courier didn't created", SC_CREATED, createStatusCode);

        ValidatableResponse loginSuccessResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginSuccessResponse.extract().path("id");

        ValidatableResponse deleteResponse = courierClient.delete(courierId);
        int deleteStatusCode = deleteResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, deleteStatusCode);

        ValidatableResponse loginNonExistingCourierResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginNonExistingCourierResponse.extract().statusCode();
        assertEquals("Status code is incorrect", SC_NOT_FOUND, loginStatusCode);
        String errorText = loginNonExistingCourierResponse.extract().path("message");
        assertEquals("Message is incorrect", ErrorTexts.getLoginErrorForFalseCredentials(), errorText);

    }


}
