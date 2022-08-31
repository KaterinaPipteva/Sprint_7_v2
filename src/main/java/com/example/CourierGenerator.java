package com.example;

import java.util.Random;
import java.util.stream.Collectors;

public class CourierGenerator {
    public static Courier getDefault(){
        return new Courier("keruak1", "12345", "jeck");
    }


    public static Courier getRandom(){
        int sizeLogin = 7;
        int sizePassword = 5;
        String symbolsLogin = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
        String symbolsPassword = "1234567890";
        String randomLogin = new Random().ints(sizeLogin, 0, symbolsLogin.length())
                .mapToObj(symbolsLogin::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        String randomPassword = new Random().ints(sizePassword, 0, symbolsPassword.length())
                .mapToObj(symbolsPassword::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());

        Courier courier = new Courier();
        courier.setLogin(randomLogin);
        courier.setPassword(randomPassword);
        courier.setFirstName("defaultName");
        return courier;
    }



    public static Courier getWithoutFirstName(){
        Courier courier = new Courier();
        courier.setLogin("keruak2");
        courier.setPassword("01234");
        return courier;
    }

    public static Courier getWithoutLogin(){
        Courier courier = new Courier();
        courier.setPassword("12345");
        courier.setFirstName("jeck");
        return courier;
    }

    public static Courier getWithoutPassword(){
        Courier courier = new Courier();
        courier.setLogin("keruak1");
        courier.setFirstName("jeck");
        return courier;
    }

    public static Courier getAlmostDefault(){
        return new Courier("keruak1", "otherPass", "otherName");
    }

    public static Courier getWithDifferentLogin(){
        return new Courier("keruak4", "12345", "jeck");
    }

    public static Courier getWithDifferentPass(){
        return new Courier("keruak1", "54321", "jeck");
    }
}
