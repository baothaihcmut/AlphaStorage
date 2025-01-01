package vn.anpha.storage.Department.Service;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

        public DepartmentDTO createDepartment(DepartmentCreationDTO departmentCreateRequest) {
                boolean company = companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                departmentCreateRequest.getCompanyId());
                User user = authoticationService.getUserByToken();
                departmentCreateRequest.setDepartmentId(UUID.randomUUID().toString());
                this.departmentRepository.insertDepartment(departmentCreateRequest);
                // userOfDepartmentService.createManger(user, department);
                return this.departmentRepository.findDepartmentById(departmentCreateRequest.getDepartmentId())
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

        }

        public UUID byteArrayToUUID(byte[] byteArray) {
                // Kiểm tra nếu mảng byte không hợp lệ hoặc không có độ dài 16 byte
                if (byteArray == null || byteArray.length != 16) {
                        throw new IllegalArgumentException("Mảng byte phải có độ dài 16 byte.");
                }

                // Sử dụng ByteBuffer để chuyển đổi byte[] thành UUID
                ByteBuffer buffer = ByteBuffer.wrap(byteArray);
                long mostSigBits = buffer.getLong(); // 8 byte đầu tiên của UUID
                long leastSigBits = buffer.getLong(); // 8 byte cuối cùng của UUID

                // Tạo và trả về UUID từ các phần mostSignificantBits và leastSignificantBits
                return new UUID(mostSigBits, leastSigBits);
        }

        public DepartmentDTO getDepartmentById(String id) {

                SecurityContext context = SecurityContextHolder.getContext();
                String emailLogin = context.getAuthentication().getName();
                DepartmentDTO departmenResponseProjection = departmentRepository
                                .findDepartmentByIdAndCheckOwn(id, emailLogin).orElseThrow(
                                                () -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                return departmenResponseProjection;
        }

        public Boolean deleteDepartmentById(String id) {
                DepartmentDTO department = this.departmentRepository.findDepartmentById(id.toString())
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                department.getCompanyId());

                // departmentRepository.delete(department);
                return true;

        }

        public DepartmentDTO updateDepartment(String departmentId, DepartmentUpdateDTO departmentUpdateRequest) {
                this.departmentRepository.updateDepartment(departmentId, departmentUpdateRequest);
                return this.departmentRepository.findDepartmentById(departmentId)
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

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
