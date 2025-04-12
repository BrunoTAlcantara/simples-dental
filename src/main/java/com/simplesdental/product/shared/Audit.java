package com.simplesdental.product.shared;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Data;

@Embeddable
@Data
public class Audit {

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @PrePersist
  private void prePersist() {
    this.createdAt = LocalDateTime.now();
  }

  @PreUpdate
  private void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }


  public void markAsDeleted() {
    this.deletedAt = LocalDateTime.now();
  }
}
