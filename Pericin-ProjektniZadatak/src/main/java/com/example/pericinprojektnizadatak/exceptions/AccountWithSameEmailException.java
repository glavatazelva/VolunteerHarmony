package com.example.pericinprojektnizadatak.exceptions;

public class AccountWithSameEmailException extends RuntimeException{

    public AccountWithSameEmailException(String message){
        super(message);
    }

}
