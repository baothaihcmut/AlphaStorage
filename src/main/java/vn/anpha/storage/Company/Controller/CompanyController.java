package vn.anpha.storage.Company.Controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
//import vn.anpha.storage.Company.DTO.request.AuthenticationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyCreationRequest;
import vn.anpha.storage.Company.DTO.request.CompanyUpdateRequest;
import vn.anpha.storage.Company.DTO.request.UpGradeCompanyRequest;
import vn.anpha.storage.Company.DTO.response.CompanyResponse;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Company.interfaceCompany.CompanyInterface;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {

        CompanyService companyService;

        @PostMapping("/create")
        ApiResponseDto<CompanyInterface> createCompany(
                        @RequestBody CompanyCreationRequest request) {
                return ApiResponseDto.<CompanyInterface>builder()
                                .result(companyService.createCompany(request))
                                .message("Create Company")
                                .success(true)
                                .build();
        }

        @PatchMapping("/update/{id}")
        ApiResponseDto<CompanyInterface> updateInfoCompany(@PathVariable String id,
                        @RequestBody CompanyUpdateRequest request) {
                return ApiResponseDto.<CompanyInterface>builder()
                                .result(companyService.updateCompanyInfo(id, request))
                                .message("update Company")
                                .success(true)
                                .build();
        }

        @PatchMapping("/upgrade/{id}")
        ApiResponseDto<CompanyInterface> updateGradeCompany(
                        @RequestBody UpGradeCompanyRequest request, @PathVariable String id) {

                return ApiResponseDto.<CompanyInterface>builder()
                                .result(companyService.updateGradeCompany(id, request))
                                .message("upgrade Company")
                                .success(true)
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
                                .result(companyService.getCompaniesOwn(pageable))
                                .message("Get All OwnCompanies")
                                .success(true)
                                .build();
        }

        @GetMapping("/get/{id}")
        ApiResponseDto<CompanyInterface> getACompanyProperties(
                        @PathVariable String id) {
                return ApiResponseDto.<CompanyInterface>builder()
                                .result(companyService.getCompany(id))
                                .message("Get Info Company by ID")
                                .success(true)
                                .build();
        }

        @DeleteMapping("/delete/{id}")
        ApiResponseDto<Boolean> deleteCompany(@PathVariable String id) {
                return ApiResponseDto.<Boolean>builder()
                                .result(companyService.deleteCompanyById(id))
                                .message("Delete Company by ID")
                                .success(true)
                                .build();
        }
}
