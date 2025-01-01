package vn.anpha.storage.User_company.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.anpha.storage.Company.Entity.Company;
//import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.File.DTO.Request.FileDetailCreationDTO;
import vn.anpha.storage.User_company.DTO.request.addUserToCompanyRequestDto;
import vn.anpha.storage.User_company.Entity.UserOfCompany;


import java.util.List;
import java.util.UUID;

@Repository
public interface UserCompanyRepository extends JpaRepository<UserOfCompany, String> {

    @Query(value= "SELECT * FROM users_of_company WHERE company_id=:id ",nativeQuery = true)
    List<UserOfCompany> findByCompany(String id);

    @Modifying
    @Query(value = """
                        INSERT INTO users_of_company (company_id, employee_id, create_at, update_at)
                        VALUES (:#{#companyId}, :#{#userId}, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)""", nativeQuery = true)
    UserOfCompany insertUserToCompany(@Param("companyId")String companyId, @Param("userId")String userId);

}
