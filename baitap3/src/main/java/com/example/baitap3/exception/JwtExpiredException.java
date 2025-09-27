package com.example.baitap3.exception;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException(String message) { super(message); }
}
