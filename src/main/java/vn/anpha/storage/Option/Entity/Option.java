package vn.anpha.storage.Option.Entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "options")
public class Option {
    @Id
    @Column(name = "option_id")
    private String optionId;

    @Column(name = "name")
    private String name;

    @Column(name = "value", columnDefinition = "INTEGER DEFAULT 0")
    private BigInteger value;

    @Column(name = "price", nullable = false)
    private BigInteger price;

    @Column(name = "description")
    private String description;
    @Column(updatable = false)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
