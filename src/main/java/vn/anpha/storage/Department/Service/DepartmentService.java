package vn.anpha.storage.Department.Service;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.Department.DTO.request.DepartmentCreationDTO;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateDTO;
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

        @Transactional
        public DepartmentDTO createDepartment(DepartmentCreationDTO departmentCreateRequest) {
                String user_id = authoticationService.GetUserIdByToken();
                if (departmentCreateRequest.getParentDepartmentId() == null) {
                        companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                        departmentCreateRequest.getCompanyId());

                } else {
                        this.userOfDepartmentService.checkManagerOfDepartment(
                                        departmentCreateRequest.getParentDepartmentId(), user_id);
                }

                try {
                        String departmentId = UUID.randomUUID().toString();
                        departmentRepository.insertDepartment(departmentCreateRequest, departmentId);
                        userOfDepartmentService.createManager(user_id, departmentId);
                        return departmentRepository.findDepartmentById(departmentId)
                                        .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw new AppException(ErrorCode.SERVER_ERROR);
                }

        }

        public DepartmentDTO getDepartmentById(String departmentId) {

                userOfDepartmentService.checkUserExistInDepartment(departmentId,
                                authoticationService.GetUserIdByToken());
                return departmentRepository.findDepartmentById(departmentId)
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        }

        public Boolean deleteDepartmentById(String id) {
                DepartmentDTO department = this.departmentRepository.findDepartmentById(id.toString())
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                department.getCompanyId());

                departmentRepository.deleteById(id);
                return true;

        }

        @Transactional
        public DepartmentDTO updateDepartment(String departmentId, DepartmentUpdateDTO departmentUpdateRequest) {
                userOfDepartmentService.checkManagerOfDepartment(
                                departmentId, authoticationService.GetUserIdByToken());
                try {
                        departmentRepository.updateDepartment(departmentId, departmentUpdateRequest);
                        return this.departmentRepository.findDepartmentById(departmentId)
                                        .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw new AppException(ErrorCode.SERVER_ERROR);
                }

        }

        public PaginateResponseDto<DepartmentDTO> GetAllDepartment(Pageable pageable, String companyId) {
                Page<DepartmentDTO> pageDepartment = departmentRepository.FindDepartmentOfCompany(

                                companyId,
                                pageable);
                var departments = pageDepartment.getContent();

                MetaPaginate pageMeta = MetaPaginate.builder()
                                .CurrentPage(pageDepartment.getNumber())
                                .PageSize(pageDepartment.getSize())
                                .TotalItems(pageDepartment.getTotalElements())
                                .TotalPages(pageDepartment.getTotalPages())
                                .build();
                PaginateResponseDto<DepartmentDTO> responseDto = new PaginateResponseDto<DepartmentDTO>();

                responseDto.setData(departments);
                responseDto.setMetaPaginate(pageMeta);
                // FindDepartmentOfComapny
                return responseDto;

        }

}
