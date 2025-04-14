package com.simplesdental.product.repository.specifications;

import com.simplesdental.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

  public static Specification<Product> nameContains(String name) {
    return (root, query, builder) -> name == null ? null :
        builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }
  
}
