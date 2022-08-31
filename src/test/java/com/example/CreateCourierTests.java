package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateCourierTests {

    Courier courier;
    Courier courierRandom;
    CourierClient courierClient;
    int courierId;
    Courier courierWithoutFirstName;
    Courier courierWithTheSameLogin;
    Courier courierWithoutLogin;
    Courier courierWithoutPassword;


    @Before
    public void setUp(){
        courier = CourierGenerator.getDefault();
        courierRandom = CourierGenerator.getRandom();
        courierClient = new CourierClient();
        courierWithoutFirstName = CourierGenerator.getWithoutFirstName();
        courierWithTheSameLogin = CourierGenerator.getAlmostDefault();
        courierWithoutLogin = CourierGenerator.getWithoutLogin();
        courierWithoutPassword = CourierGenerator.getWithoutPassword();

    }

    @After
    public void tearDown(){
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Creating new courier with proper parameters")
    @Description("Basic test for create courier via api/v1/courier and checking login with this courier")
    public void createNewCourierSuccessTest(){
        ValidatableResponse createResponse = courierClient.create(courierRandom);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Status cod is incorrect",SC_CREATED , createStatusCode);
        boolean isCreated = createResponse.extract().path("ok");
        assertTrue("Courier is not created", isCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierRandom));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status cod is incorrect", SC_OK, loginStatusCode);
        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Creating new courier without firstName")
    @Description("Test for create courier with login and password only and checking log in with this courier")
    public void createCourierWithoutOptionalParameterSuccessTest(){
        ValidatableResponse createResponse = courierClient.create(courierWithoutFirstName);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals("Status cod is incorrect",SC_CREATED , createStatusCode);
        boolean isCreated = createResponse.extract().path("ok");
        assertTrue("Courier is not created", isCreated);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierWithoutFirstName));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals("Status cod is incorrect", SC_OK, loginStatusCode);
        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Unsuccessful creating two couriers with the same parameters")
    @Description("Test for create courier one and than courier two with the same parameters as courier one had")
    public void createTwoEqualCouriersFailureTest() {
        ValidatableResponse createFirstResponse = courierClient.create(courierRandom);
        int createFirstStatusCode = createFirstResponse.extract().statusCode();
        assertEquals("Status cod is incorrect",SC_CREATED , createFirstStatusCode);
        boolean isCreatedFirst = createFirstResponse.extract().path("ok");
        assertTrue("Courier is not created", isCreatedFirst);

        ValidatableResponse createSecondResponse = courierClient.create(courierRandom);
        int createSecondStatusCode = createSecondResponse.extract().statusCode();
        assertEquals("Status cod is incorrect",SC_CONFLICT , createSecondStatusCode);
        String errorTextCreatedSecond = createSecondResponse.extract().path("message");
        assertEquals(ErrorTexts.getCreateErrorForExistingParameters(), errorTextCreatedSecond);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courierRandom));
        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Unsuccessful creating two couriers with equal login")
    @Description("Test for create courier one and than courier two with the same login but different password as courier one had")
    public void createTwoCouriersWithEqualsLoginsFailureTest() {
        ValidatableResponse createFirstResponse = courierClient.create(courier);
        int createFirstStatusCode = createFirstResponse.extract().statusCode();
        assertEquals("Status cod is incorrect",SC_CREATED, createFirstStatusCode);
        boolean isCreatedFirst = createFirstResponse.extract().path("ok");
        assertTrue("Courier is not created", isCreatedFirst);

        ValidatableResponse createSecondResponse = courierClient.create(courierWithTheSameLogin);
        int createSecondStatusCode = createSecondResponse.extract().statusCode();
        assertEquals("Status cod is incorrect",SC_CONFLICT, createSecondStatusCode);
        String errorTextCreatedSecond = createSecondResponse.extract().path("message");
        assertEquals(ErrorTexts.getCreateErrorForExistingParameters(), errorTextCreatedSecond);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

    }

    @Test
    @DisplayName("Unsuccessful creating new courier without required parameters")
    @Description("Test for create courier without login and then without password")
    public void createCourierWithoutRequiredParametersFailureTest() {
        ValidatableResponse createFirstResponse = courierClient.create(courierWithoutLogin);
        int createFirstStatusCode = createFirstResponse.extract().statusCode();
        assertEquals("Status cod should be 400",SC_BAD_REQUEST, createFirstStatusCode);
        String firstErrorTextCreated = createFirstResponse.extract().path("message");
        assertEquals(ErrorTexts.getCreateErrorForAbsenceRequiredParameters(), firstErrorTextCreated);

        ValidatableResponse createSecondResponse = courierClient.create(courierWithoutPassword);
        int createSecondStatusCode = createSecondResponse.extract().statusCode();
        assertEquals("Status cod should be 400",SC_BAD_REQUEST, createSecondStatusCode);
        String secondErrorTextCreated = createSecondResponse.extract().path("message");
        assertEquals(ErrorTexts.getCreateErrorForAbsenceRequiredParameters(), secondErrorTextCreated);
    }

}
