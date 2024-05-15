package com.bsep.bezbednosttim32.exceptions;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(String message){
        super(message);
    }
}
