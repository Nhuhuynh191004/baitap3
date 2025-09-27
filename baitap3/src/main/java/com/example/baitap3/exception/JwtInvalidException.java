package com.example.baitap3.exception;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String message) { super(message); }
}
