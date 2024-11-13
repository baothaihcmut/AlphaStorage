package vn.anpha.storage.Detail.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.User.Entity.User;

@ToString
@Getter
@Setter
@Entity
@Slf4j
@Table(name = "detailUsers")
public class DetailUser {
    @Id
    private UUID id;

    @OneToOne(optional = false)
    @MapsId
    @JsonBackReference // Đánh dấu là thực thể con
    private User user;

    private long total_size;
    private long limit_size;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public DetailUser() {

    }

    @PrePersist
    public void handleBeforeCreate() {
        long Init_500Mb = 524288000L;

        this.limit_size = Init_500Mb;
        this.total_size = 0;

    }

}