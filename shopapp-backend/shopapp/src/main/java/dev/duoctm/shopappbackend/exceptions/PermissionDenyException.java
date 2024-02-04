package dev.duoctm.shopappbackend.exceptions;

public class PermissionDenyException extends  Exception {
    public PermissionDenyException(String message){
        super(message);
    }

}
