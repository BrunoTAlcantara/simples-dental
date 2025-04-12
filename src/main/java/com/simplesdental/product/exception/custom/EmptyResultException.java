package com.simplesdental.product.exception.custom;

public class EmptyResultException extends RuntimeException {
    public EmptyResultException(String message) {
        super(message);
    }
}

