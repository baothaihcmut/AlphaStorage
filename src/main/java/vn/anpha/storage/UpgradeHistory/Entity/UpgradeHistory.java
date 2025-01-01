package vn.anpha.storage.UpgradeHistory.Entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Option.Entity.Option;

@Entity
@Table(name = "upgrade_histories")
@IdClass(UpgradeHistoryId.class)
public class UpgradeHistory {
    @Id
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private Company company;

    @Id
    @ManyToOne
    @JoinColumn(name = "option_id", referencedColumnName = "option_id")
    private Option option;

    @CreationTimestamp
    private LocalDateTime time;

}
