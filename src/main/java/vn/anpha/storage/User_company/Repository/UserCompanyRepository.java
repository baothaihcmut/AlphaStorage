package vn.anpha.storage.User_company.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.anpha.storage.Company.Entity.Company;
//import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_company.Entity.UserOfCompany;


import java.util.List;
import java.util.UUID;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserOfCompany, UUID> {

//    boolean existsUserOfCompanyById(User user);

    List<UserOfCompany> findByCompany(Company company);
}
