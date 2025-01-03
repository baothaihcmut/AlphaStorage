package vn.anpha.storage.User_company.Repository;

import java.time.LocalDateTime;

public interface UserCompanyResponseProjection {
    String getEmployee() ;
    String getCompany();
    boolean isStatus();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
