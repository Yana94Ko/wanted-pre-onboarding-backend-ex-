package wanted.preonboarding.backend.global;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

}