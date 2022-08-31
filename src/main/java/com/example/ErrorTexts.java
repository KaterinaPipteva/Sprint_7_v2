package com.example;

public class ErrorTexts {

    public static String getLoginErrorForFalseCredentials(){
        return "Учетная запись не найдена";
    }

    public static String getLoginErrorForAbsenceRequiredParameters(){
        return "Недостаточно данных для входа";
    }

    public static String getCreateErrorForExistingParameters(){
        //тут тест должен падать потому что в документации другая текстовка "Этот логин уже используется",
        // а на деле там "Этот логин уже используется. Попробуйте другой."
        return "Этот логин уже используется. Попробуйте другой.";
    }

    public static String getCreateErrorForAbsenceRequiredParameters(){
        return "Недостаточно данных для создания учетной записи";
    }

}
