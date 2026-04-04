package vn.deposit.core.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.UUID;

@Data
@MappedSuperclass
public class BaseEntity {

  @Id
  private String id;

  @CreatedDate
  private Long createdAt;

  @CreatedBy
  private String createdBy;

  @PrePersist
  private void ensureId() {
    if (this.getId() == null || this.getId().isEmpty()) {
      this.setId(UUID.randomUUID().toString());
    }
  }

}
