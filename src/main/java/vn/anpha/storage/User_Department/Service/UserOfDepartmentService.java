package vn.anpha.storage.User_Department.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.DTO.request.UserDepartmentCreate;
import vn.anpha.storage.User_Department.DTO.request.UserDepartmentUpdate;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;
import vn.anpha.storage.User_Department.Repository.UserDepartmentResponseProjection;
import vn.anpha.storage.User_Department.Repository.UserOfDepartmentRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.MetaPaginate;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserOfDepartmentService {
    private UserOfDepartmentRepository userOfDepartmentRepository;
    private AuthoticationService authoticationService;
    private CompanyService companyService;
    private DepartmentRepository departmentRepository;

    public void checkManagerOfDepartment(String departmentId, String user_id) {

        long count = userOfDepartmentRepository.checkManagerDepartment(departmentId, user_id);
        if (count == 0) {
            throw new AppException(ErrorCode.USER_NOT_OWNDEPARTMENT);
        }
    }

    public void checkUserExistInDepartment(String departmentId, String userId) {
        userOfDepartmentRepository.findUserOfDepartment(userId,
                departmentId).orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_EXISTED));

    }

    @Transactional
    public DepartmentUser createManager(String user, String departmentId) {
        userOfDepartmentRepository.insertUserToDepartment(departmentId, user, true);
        return userOfDepartmentRepository.findUserOfDepartment(user, departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));
    }

    public DepartmentUser updateUserOfDepartment(
            UserDepartmentUpdate updateDTO) {
        DepartmentDTO department = this.departmentRepository.findDepartmentById(updateDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        companyService.checkOwnCompany(authoticationService.getUserByToken(),
                department.getCompanyId());
        if (department.getParentDepartmentId() != null) {
            this.checkManagerOfDepartment(department.getParentDepartmentId(),
                    authoticationService.GetUserIdByToken());
        } else {
            companyService.checkOwnCompany(authoticationService.getUserByToken(), department.getCompanyId());
        }
        DepartmentUser userOfDepartment = this.userOfDepartmentRepository.findUserOfDepartment(
                updateDTO.getUserId(),
                updateDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));
        userOfDepartment.setManager(updateDTO.isManager());
        userOfDepartmentRepository.save(userOfDepartment);
        return userOfDepartment;

    }

    @Transactional
    public DepartmentUser createUserOfDepartment(UserDepartmentCreate userOfDepartmentCreate) {
        DepartmentDTO department = this.departmentRepository
                .findDepartmentById(userOfDepartmentCreate.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

        // Kiểm cha có thuộc công ty không
        companyService.checkUserInCompany(userOfDepartmentCreate.getUserId(), department.getCompanyId());

        // Kiểm Người Thao tác có quyền thêm user vào phòng ban không
        this.checkManagerOfDepartment(userOfDepartmentCreate.getDepartmentId(),
                authoticationService.GetUserIdByToken());

        // Kiểm tra khi thêm quản lý vào department =>Người thực hiện phải có quyền quản
        // lý ở lớp cao hơn
        if (userOfDepartmentCreate.isManager()) {
            if (department.getParentDepartmentId() != null) {
                this.checkManagerOfDepartment(department.getParentDepartmentId(),
                        authoticationService.GetUserIdByToken());
            } else {
                companyService.checkOwnCompany(authoticationService.getUserByToken(), department.getCompanyId());
            }
        }

        userOfDepartmentRepository.insertUserToDepartment(userOfDepartmentCreate.getDepartmentId(),
                userOfDepartmentCreate.getUserId(), userOfDepartmentCreate.isManager());

        return userOfDepartmentRepository
                .findUserOfDepartment(userOfDepartmentCreate.getUserId(), userOfDepartmentCreate.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));

    }

    public void deleteUserOfDepartmentBy(String userId, String departmentId) {
        this.checkManagerOfDepartment(departmentId, authoticationService.GetUserIdByToken());
        DepartmentUser departmentUser = userOfDepartmentRepository.findUserOfDepartment(userId, departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));

        DepartmentDTO department = this.departmentRepository
                .findDepartmentById(departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));

        if (departmentUser.isManager()) {
            if (department.getParentDepartmentId() != null) {
                this.checkManagerOfDepartment(department.getParentDepartmentId(),
                        authoticationService.GetUserIdByToken());
            } else {
                companyService.checkOwnCompany(authoticationService.getUserByToken(), department.getCompanyId());
            }
        }
        userOfDepartmentRepository.delete(departmentUser);

    }

    public DepartmentUser findUserAndDepartment(String user_id, String department_id) {
        return userOfDepartmentRepository.findUserOfDepartment(user_id, department_id).orElse(null);
    }

    public PaginateResponseDto<UserDepartmentResponseProjection> GetAllUser(Pageable pageable, String DepartmentId) {
        Page<UserDepartmentResponseProjection> pageUser = userOfDepartmentRepository
                .findAllUserOfDepartment(DepartmentId, pageable); // tư
        // set
        // limit
        // offset
        var userOfDepartment = pageUser.getContent();
        MetaPaginate pageMeta = MetaPaginate.builder()
                .CurrentPage(pageUser.getNumber())
                .PageSize(pageUser.getSize())
                .TotalItems(pageUser.getTotalElements())
                .TotalPages(pageUser.getTotalPages())
                .build();
        PaginateResponseDto<UserDepartmentResponseProjection> responseDto = new PaginateResponseDto<UserDepartmentResponseProjection>();
        responseDto.setData(userOfDepartment);
        responseDto.setMetaPaginate(pageMeta);

        return responseDto;

    }

}
