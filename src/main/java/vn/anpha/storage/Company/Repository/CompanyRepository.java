package vn.anpha.storage.Company.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.User.Entity.User;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    boolean existsCompanyByName(String name);

    Company findCompanyByName(String name);

    List<Company> findAllByCreateBy(String createBy);
}
