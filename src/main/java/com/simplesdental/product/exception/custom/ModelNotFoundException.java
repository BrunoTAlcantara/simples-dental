package com.simplesdental.product.exception.custom;

public class ModelNotFoundException extends  RuntimeException {
    public ModelNotFoundException(String name, Long id) {
        super(String.format("%s com ID %d não encontrado", name, id));
    }
}
