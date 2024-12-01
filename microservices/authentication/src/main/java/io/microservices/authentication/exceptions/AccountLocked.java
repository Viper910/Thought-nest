package io.microservices.authentication.exceptions;

public class AccountLocked extends RuntimeException {
    
    public AccountLocked(){
        super("Wrong Password.");
    }
    
    public AccountLocked(String message){
        super(message);
    }
    
}
