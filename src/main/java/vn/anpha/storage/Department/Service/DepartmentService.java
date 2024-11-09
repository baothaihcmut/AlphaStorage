package vn.anpha.storage.Department.Service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateRequest;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Mapper.DepartmentMapper;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.Service.UserOfDepartmentService;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.MetaPaginate;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {
        DepartmentRepository departmentRepository;
        DepartmentMapper departmentMapper;
        AuthoticationService authoticationService;
        UserOfDepartmentService userOfDepartmentService;
        CompanyService companyService;

        public DepartmenResponse createDepartment(DepartmentCreateRequest departmentCreateRequest) {
                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                departmentCreateRequest.getCompanyid());
                User user = authoticationService.getUserByToken();
                Department department = departmentMapper.toDepartment(departmentCreateRequest);
                department = departmentRepository.save(department);
                userOfDepartmentService.createManger(user, department);
                DepartmenResponse response = departmentMapper.toDepartmentResponse(department);
                return response;

        }

        public DepartmenResponse getDepartmentById(UUID id) {

                Department department = departmentRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                department.getCompany().getCompanyId());
                DepartmenResponse response = departmentMapper.toDepartmentResponse(department);
                return response;
        }

        public void deleteDepartmentById(UUID id) {
                Department department = departmentRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                department.getCompany().getCompanyId());

                departmentRepository.delete(department);

        }

        public Department updateDepartmentName(DepartmentUpdateRequest departmentUpdateRequest) {

                Department department = departmentRepository.findById(departmentUpdateRequest.getDepartmentId())
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                department.getCompany().getCompanyId());

                department.setName(departmentUpdateRequest.getName());
                return departmentRepository.save(department);

        }

        public PaginateResponseDto<Department> GetAllDepartment(Pageable pageable) {
                Page<Department> pageUser = departmentRepository.findAll(pageable); // tư set limit offset
                var userOfDepartment = pageUser.getContent();
                MetaPaginate pageMeta = MetaPaginate.builder()
                                .CurrentPage(pageUser.getNumber())
                                .PageSize(pageUser.getSize())
                                .TotalItems(pageUser.getTotalElements())
                                .TotalPages(pageUser.getTotalPages())
                                .build();
                PaginateResponseDto<Department> responseDto = new PaginateResponseDto<Department>();
                responseDto.setData(userOfDepartment);
                responseDto.setMetaPaginate(pageMeta);

                return responseDto;

        }

}
