package com.example;


public class OrderGenerator {

    public static Order getDefaultBesidesColor(String[] color){
        Order order = new Order("Bellatrix", "Lestrange", "Лондон, ул Вепря, 45 кв 666", "Чертаново", "+7 800 555 35 35", 5, "2020-09-09", "The Dark Lord will rise again!");
        order.setColor(color);
        return order;
    }
}
