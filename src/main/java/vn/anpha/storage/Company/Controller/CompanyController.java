package vn.anpha.storage.Company.Controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.UpGradeCompanyRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {

        CompanyService companyService;

        @PostMapping("/create")
        ApiResponseDto<CompanyResponse> createCompany(
                        @RequestBody CompanyCreationRequest request) {
                return ApiResponseDto.<CompanyResponse>builder()
                                .result(companyService.createCompany(request))
                                .build();
        }

        @PatchMapping("/update/{id}")
        ApiResponseDto<CompanyResponse> updateInfoCompany(@PathVariable UUID id,
                        @RequestBody UpGradeCompanyRequest request) {
                return ApiResponseDto.<CompanyResponse>builder()
                                .result(companyService.updateGradeCompany(id, request))
                                .build();
        }

        @PatchMapping("/upgrade/{id}")
        ApiResponseDto<CompanyResponse> updateGradeCompany(
                        @RequestBody UpGradeCompanyRequest request, @PathVariable UUID id) {

                return ApiResponseDto.<CompanyResponse>builder()
                                .result(companyService.updateGradeCompany(id, request))
                                .build();
        }

        @GetMapping("/getOwn")
        ApiResponseDto<PaginateResponseDto> getCompanyProperties(
                @RequestParam("current") Optional<String> currentOptional,
                @RequestParam("pageSize") Optional<String> pageSizeOptional) {
                int current = currentOptional.map(Integer::parseInt).orElse(1);
                int pageSize = pageSizeOptional.map(Integer::parseInt).orElse(10);
                Pageable pageable = PageRequest.of(current - 1, pageSize);
                return ApiResponseDto.<PaginateResponseDto>builder()
                                .result(companyService.getCompanies(pageable))
                                .build();
        }

        @GetMapping("/get/{id}")
        ApiResponseDto<CompanyResponse> getACompanyProperties(
                        @PathVariable UUID id) {
                return ApiResponseDto.<CompanyResponse>builder()
                                .result(companyService.getCompany(id))
                                .build();
        }

        @DeleteMapping("/delete/{id}")
        ApiResponseDto<Boolean> deleteCompany(@PathVariable UUID id) {
                return ApiResponseDto.<Boolean>builder()
                                .result(companyService.deleteCompanyById(id))
                                .build();
        }
}
