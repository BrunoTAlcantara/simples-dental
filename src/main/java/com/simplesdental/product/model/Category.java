package com.simplesdental.product.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.simplesdental.product.shared.Audit;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;


  @OneToMany(mappedBy = "category")
  @JsonManagedReference
  private List<Product> products;

  @Embedded
  private Audit audit;


}