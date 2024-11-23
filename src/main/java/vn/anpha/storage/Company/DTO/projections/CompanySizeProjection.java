package vn.anpha.storage.Company.DTO.projections;

import java.math.BigInteger;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

public interface CompanySizeProjection {
    @Value("#{T(java.util.UUID).nameUUIDFromBytes(target.companyId)}")
    UUID getCompanyId();

    String getName();

    BigInteger getTotalSize();

    BigInteger getLimitSize();

    Boolean getHasVersion();
}
