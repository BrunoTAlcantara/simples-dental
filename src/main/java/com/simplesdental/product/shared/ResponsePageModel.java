package com.simplesdental.product.shared;

import java.util.List;

public record ResponsePageModel<T>(
    long totalItems,
    int currentPage,
    int totalPages,
    List<T> items
) {

}