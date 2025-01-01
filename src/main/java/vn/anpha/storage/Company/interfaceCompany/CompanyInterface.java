package vn.anpha.storage.Company.interfaceCompany;

import java.math.BigInteger;
import java.time.LocalDateTime;

public interface CompanyInterface {

     String getCompanyId();

     String getName();

     String getDescription();

     Boolean getHasVersion();

     BigInteger getTotalSize();

     BigInteger getLimitSize();

     String getOwnerId();

     LocalDateTime getCreatedAt();

     LocalDateTime getUpdatedAt();
}
