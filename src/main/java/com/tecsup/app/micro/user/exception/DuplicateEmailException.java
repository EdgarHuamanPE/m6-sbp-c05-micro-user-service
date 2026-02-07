package com.tecsup.app.micro.user.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String email){
        super("User duplicate with email :"+ email);
    }
}
