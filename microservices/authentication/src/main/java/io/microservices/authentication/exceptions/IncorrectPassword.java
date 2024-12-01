package io.microservices.authentication.exceptions;

public class IncorrectPassword extends RuntimeException {
    
    public IncorrectPassword(){
        super("Wrong Password.");
    }
    
    public IncorrectPassword(String message){
        super(message);
    }
    
}
