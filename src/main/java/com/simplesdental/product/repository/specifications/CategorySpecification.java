package com.simplesdental.product.repository.specifications;

import com.simplesdental.product.model.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {

  public static Specification<Category> nameContains(String name) {
    return (root, query, builder) -> name == null ? null :
        builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
  }
}
