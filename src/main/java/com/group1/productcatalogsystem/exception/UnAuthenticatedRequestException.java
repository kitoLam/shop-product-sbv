package com.group1.productcatalogsystem.exception;

public class UnAuthenticatedRequestException extends RuntimeException{
    public UnAuthenticatedRequestException(String message) { super(message); }
}
