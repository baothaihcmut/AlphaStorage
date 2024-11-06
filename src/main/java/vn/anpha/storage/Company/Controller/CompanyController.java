package vn.anpha.storage.Company.Controller;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {

    CompanyService companyService;

    @PostMapping("/create")
    ApiResponseDto<CompanyResponse> createCompany(
        @RequestBody CompanyCreationRequest request
    ) {
        return ApiResponseDto.<CompanyResponse>builder()
            .result(companyService.createCompany(request))
            .build();
    }

    @PostMapping("/update")
    ApiResponseDto<CompanyResponse> updateCompany(
        @RequestBody CompanyUpdateRequest request
    ) {
        return ApiResponseDto.<CompanyResponse>builder()
            .result(companyService.updateCompany(request))
            .build();
    }

    @GetMapping("/GetListCompany")
    ApiResponseDto<List<CompanyResponse>> getCompanyProperties() {
        return ApiResponseDto.<List<CompanyResponse>>builder()
            .result(companyService.getCompanies())
            .build();
    }

    @GetMapping("/GetCompany/{id}")
    ApiResponseDto<CompanyResponse> getACompanyProperties(
        @PathVariable UUID id
    ) {
        return ApiResponseDto.<CompanyResponse>builder()
            .result(companyService.getCompany(id))
            .build();
    }
}
