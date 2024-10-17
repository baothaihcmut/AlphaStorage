package vn.anpha.storage.History.Entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.anpha.storage.File.Entity.File;

@ToString
@Getter
@Setter
@Entity
@Table(name = "logUsers")
public class LogUser {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(nullable = true, unique = true, updatable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private ActionEnum action;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false, nullable = false)
    private StatusEnum status;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file;
}
