package vn.anpha.storage.User_Department.Service;

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
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.DTO.Request.UserDepartmentUpdate;
import vn.anpha.storage.User_Department.Entity.DepartmentUser;
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

    private void checkManagerOfDepartment(Department department) {
        User userToken = authoticationService.getUserByToken();
        DepartmentUser own = userOfDepartmentRepository.findUserOfDepartment(userToken.getUserId(),
                department.getDepartmentId()).orElse(null);

        boolean isOwner = own != null && own.isManager();
        if (!isOwner) {
            throw new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS);
        }
    }

    private void checkUserExistInDepartment(UUID userId, Department department) {
        userOfDepartmentRepository.findUserOfDepartment(userId,
                department.getDepartmentId()).orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_EXISTED));

    }

    public DepartmentUser createManger(User user, Department department) {
        DepartmentUser userOfDepartment = userOfDepartmentRepository.findUserOfDepartment(user.getUserId(),
                department.getDepartmentId()).orElse(null);
        if (userOfDepartment == null) {
            userOfDepartment = DepartmentUser.builder().department(department).user(user).isManager(true).build();
            userOfDepartmentRepository.save(userOfDepartment);
            return userOfDepartment;
        } else {
            if (userOfDepartment.isManager() == false) {
                userOfDepartment.setManager(true);
                userOfDepartmentRepository.save(userOfDepartment);
            }
            return userOfDepartment;
        }
    }

    public DepartmentUser updateUserOfDepartment(
            UserDepartmentUpdate updateDTO) {
        Department department = departmentRepository.findDepartmentById(updateDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        companyService.checkOwnCompany(authoticationService.getUserByToken(),
                department.getCompany().getCompanyId());
        DepartmentUser userOfDepartment = this.userOfDepartmentRepository.findUserOfDepartment(
                updateDTO.getUserId(),
                updateDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));
        userOfDepartment.setManager(updateDTO.isManager());
        userOfDepartmentRepository.save(userOfDepartment);
        return userOfDepartment;

    }

    public DepartmentUser createUserOfDepartment(User user, Department department) {
        // check if user exist in deparment
        this.checkManagerOfDepartment(department);
        this.checkUserExistInDepartment(user.getUserId(), department);
        DepartmentUser newUserOfDepartment = DepartmentUser.builder().department(department).user(user).isManager(false)
                .build();
        userOfDepartmentRepository.save(newUserOfDepartment);
        return newUserOfDepartment;
    }

    public void deleteUserOfDepartmentBy(UUID userId, UUID departmentId) {
        DepartmentUser departmentUser = userOfDepartmentRepository.findUserOfDepartment(userId, departmentId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));

        userOfDepartmentRepository.delete(departmentUser);

    }

    public DepartmentUser findUserAndDepartment(UUID user_id, UUID department_id) {
        return userOfDepartmentRepository.findUserOfDepartment(user_id, department_id).orElse(null);
    }

    public PaginateResponseDto<DepartmentUser> GetAllUser(Pageable pageable) {
        Page<DepartmentUser> pageUser = userOfDepartmentRepository.findAll(pageable); // tư set limit offset
        var userOfDepartment = pageUser.getContent();
        MetaPaginate pageMeta = MetaPaginate.builder()
                .CurrentPage(pageUser.getNumber())
                .PageSize(pageUser.getSize())
                .TotalItems(pageUser.getTotalElements())
                .TotalPages(pageUser.getTotalPages())
                .build();
        PaginateResponseDto<DepartmentUser> responseDto = new PaginateResponseDto<DepartmentUser>();
        responseDto.setData(userOfDepartment);
        responseDto.setMetaPaginate(pageMeta);

        return responseDto;

    }

}
