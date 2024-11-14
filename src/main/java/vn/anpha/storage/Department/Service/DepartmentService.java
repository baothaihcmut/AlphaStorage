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
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.DTO.request.DepartmentCreateRequest;
import vn.anpha.storage.Department.DTO.request.DepartmentUpdateRequest;
import vn.anpha.storage.Department.DTO.response.DepartmenResponse;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Mapper.DepartmentMapper;
import vn.anpha.storage.Department.Repository.DepartmenResponseProjection;
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
                Company company = companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                departmentCreateRequest.getCompanyId());
                User user = authoticationService.getUserByToken();
                Department department = departmentMapper.toDepartment(departmentCreateRequest);
                department.setCompany(company);
                department = departmentRepository.save(department);
                userOfDepartmentService.createManger(user, department);
                DepartmenResponse response = departmentMapper.toDepartmentResponse(department);
                return response;

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

        public DepartmenResponse getDepartmentById(UUID id) {

                SecurityContext context = SecurityContextHolder.getContext();
                String emailLogin = context.getAuthentication().getName();
                DepartmenResponseProjection departmenResponseProjection = departmentRepository
                                .findDepartmentByIdAndCheckOwn(id, emailLogin);

                DepartmenResponse response = DepartmenResponse.builder()
                                .departmentId(byteArrayToUUID(departmenResponseProjection.getDepartmentId()))
                                .name(departmenResponseProjection.getName())
                                .build();
                return response;
        }

        public Boolean deleteDepartmentById(UUID id) {
                Department department = departmentRepository.findById(id)
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                department.getCompany().getCompanyId());

                departmentRepository.delete(department);
                return true;

        }

        public DepartmenResponse updateDepartmentName(DepartmentUpdateRequest departmentUpdateRequest) {

                Department department = departmentRepository.findById(departmentUpdateRequest.getDepartmentId())
                                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

                companyService.checkOwnCompany(authoticationService.getUserByToken(),
                                department.getCompany().getCompanyId());

                department.setName(departmentUpdateRequest.getName());
                departmentRepository.save(department);
                return departmentMapper.toDepartmentResponse(department);

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
