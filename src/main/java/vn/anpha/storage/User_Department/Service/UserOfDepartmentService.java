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
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.Dto.ResponseDto.UserResponseDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User.Service.UserService;
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
    UserOfDepartmentRepository userOfDepartmentRepository;
    AuthoticationService authoticationService;

    public DepartmentUser createManger(User user, Department department) {
        DepartmentUser userOfDepartment = userOfDepartmentRepository.findUserOfDepartment(user.getUserId(),
                department.getDepartmentId());
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

    public DepartmentUser updateUserOfDepartment(UUID id, boolean isManager) {
        DepartmentUser userOfDepartment = this.userOfDepartmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));
        userOfDepartment.setManager(isManager);
        userOfDepartmentRepository.save(userOfDepartment);
        return userOfDepartment;

    }

    public DepartmentUser createUserOfDepartment(User user, Department department) {
        // thiếu logic chưa xong
        DepartmentUser userOfDepartment = userOfDepartmentRepository.findUserOfDepartment(user.getUserId(),
                department.getDepartmentId());
        boolean isOwner = authoticationService.getUserByToken().getUserId() == user.getUserId();
        if (!isOwner) {
            throw new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS);
        }
        if (userOfDepartment == null) {
            userOfDepartment = DepartmentUser.builder().department(department).user(user).isManager(false).build();
            userOfDepartmentRepository.save(userOfDepartment);
            return userOfDepartment;
        } else {
            throw new AppException(ErrorCode.USER_OF_DEPARTMENT_EXISTED);
        }
    }

    public void deleteUserOfDepartmentBy(UUID userOfDepartment) {
        DepartmentUser departmentUser = userOfDepartmentRepository.findUserById(userOfDepartment);
        if (departmentUser != null) {
            userOfDepartmentRepository.delete(departmentUser);
        } else {
            throw new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED);
        }
    }

    public DepartmentUser findUserAndDepartment(UUID user_id, UUID department_id) {
        return userOfDepartmentRepository.findUserOfDepartment(user_id, department_id);
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
