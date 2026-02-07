package com.tecsup.app.micro.user.exception;

public class NotFoundUser extends  RuntimeException{
    public NotFoundUser(Long id){
        super("Not Found User with id:" + id);
    }
}
