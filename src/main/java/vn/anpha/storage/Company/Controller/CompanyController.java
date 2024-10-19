package vn.anpha.storage.Company.Controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.*;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyGetRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.response.CompanyCreationResponse;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.DTO.response.CompanyUpdateResponse;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

    @PostMapping("/create")
    ApiResponseDto<CompanyCreationResponse> createCompany(@RequestBody CompanyCreationRequest request) {
        return ApiResponseDto.<CompanyCreationResponse>builder()
                .result(companyService.createCompany(request))
                .build();
    }

    @PostMapping("/update")
    ApiResponseDto<CompanyUpdateResponse> updateCompany(@RequestBody CompanyUpdateRequest request) {
        return ApiResponseDto.<CompanyUpdateResponse>builder()
                .result(companyService.updateCompany(request))
                .build();
    }

    @GetMapping("/GetListCompany")
    ApiResponseDto<List<CompanyResponse>> getCompanyProperties(@RequestBody CompanyGetRequest request) {
        return ApiResponseDto.<List<CompanyResponse>>builder()
                .result(companyService.getCompany())
                .build();
    }
}
