package vn.anpha.storage.Company.Mapper;

import org.mapstruct.Mapper;

import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.response.CompanyCreationResponse;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.DTO.response.CompanyUpdateResponse;
import vn.anpha.storage.Company.Entity.Company;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CompanyMapper {
    Company toCompany(CompanyCreationRequest company);

    CompanyCreationResponse toCompanyCreationResponse(Company company);

    CompanyUpdateResponse toCompanyUpdateResponse(Company company);

    CompanyResponse toCompanyResponse(Company company);
}
