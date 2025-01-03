package vn.anpha.storage.Tag.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import vn.anpha.storage.Company.Entity.Company;

@Entity
@Table(name = "tag_companies")
public class TagCompany {
    @Id
    private String tagId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
