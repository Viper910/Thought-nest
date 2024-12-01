package io.microservices.authentication.exceptions;

public class UserEmailAlreadyExistException extends RuntimeException {
    
    public UserEmailAlreadyExistException(){
        super("User email already exist try to login using this email.");
    }
    
    public UserEmailAlreadyExistException(String message){
        super(message);
    }
    
}
