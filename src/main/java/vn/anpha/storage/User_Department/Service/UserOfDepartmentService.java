package vn.anpha.storage.User_Department.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User_Department.DTO.Request.UserDepartmentCreate;
import vn.anpha.storage.User_Department.DTO.Request.UserDepartmentUpdate;
import vn.anpha.storage.User_Department.DTO.projection.DepartmentUserDto;
import vn.anpha.storage.User_Department.DTO.projection.DepartmentUserDtoImpl;
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
    private EntityManager entityManager;

    public void checkManagerOfDepartment(String departmentId, String user_id) {

        long count = userOfDepartmentRepository.checkManagerDepartment(departmentId, user_id);
        if (count == 0) {
            throw new AppException(ErrorCode.USER_NOT_OWNDEPARTMENT);
        }
    }

    public void insertMultipleUsersToDepartment(List<DepartmentUserDtoImpl> listUserDtoImpls) {

        try {
            if (listUserDtoImpls == null || listUserDtoImpls.isEmpty()) {
                return; // Không có người dùng để chèn
            }

            StringBuilder query = new StringBuilder(
                    "INSERT INTO department_of_user (department_id, user_id, created_at, updated_at, is_manager) VALUES ");

            for (int i = 0; i < listUserDtoImpls.size(); i++) {
                DepartmentUserDtoImpl user = listUserDtoImpls.get(i);
                query.append(
                        String.format("('%s', '%s', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, %b)", user.getDepartmentId(),
                                user.getUserId(), user.getIsManager() ? 1 : 0));

                if (i < listUserDtoImpls.size() - 1) {
                    query.append(", ");
                }
            }

            // Sử dụng câu query đã xây dựng để thực thi với EntityManager hoặc JdbcTemplate
            entityManager.createNativeQuery(query.toString()).executeUpdate();
        } catch (Exception e) {
            log.error("Error creating: " + e);
            throw new AppException(ErrorCode.CREATE_USER_OF_DEPARTMENT_ERROR);
        }

    }

    private List<DepartmentUserDto> getManagerOfParentDepartment(String parentDepartmentId) {
        return userOfDepartmentRepository.findManagerOfDepartment(parentDepartmentId);
    }

    // Phương thức chuyển đổi danh sách DepartmentUserDto sang DepartmentUserDtoImpl
    private List<DepartmentUserDtoImpl> convertToDepartmentUserDtoImpl(
            List<DepartmentUserDto> managerOfDepartmentParent, String departmentId) {
        List<DepartmentUserDtoImpl> managerOfDepartmentChildren = new ArrayList<>();
        for (DepartmentUserDto manager : managerOfDepartmentParent) {
            DepartmentUserDtoImpl managerChildren = new DepartmentUserDtoImpl(
                    manager.getUserId(),
                    departmentId,
                    true);
            managerOfDepartmentChildren.add(managerChildren);
        }
        return managerOfDepartmentChildren;
    }

    @Transactional
    public boolean createManager(String userId, DepartmentDTO department) {
        try {
            if (department.getParentDepartmentId() != null) {
                // Lấy danh sách các quản lý của phòng ban cha

                List<DepartmentUserDto> managerOfDepartmentParent = getManagerOfParentDepartment(
                        department.getParentDepartmentId());

                // Chuyển đổi danh sách các quản lý từ DTO gốc sang DTO mới
                List<DepartmentUserDtoImpl> managerOfDepartmentChildren = convertToDepartmentUserDtoImpl(
                        managerOfDepartmentParent, department.getDepartmentId());

                // Chèn vào phòng ban mới
                insertMultipleUsersToDepartment(managerOfDepartmentChildren);
                return true;
            } else {

                // Nếu không có phòng ban cha, trực tiếp thêm người dùng vào phòng ban
                userOfDepartmentRepository.insertUserToDepartment(department.getDepartmentId(), userId, true);
                return true;
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_USER_OF_DEPARTMENT_ERROR);
        }
    }

    // Phương thức lấy danh sách các quản lý của phòng ban cha

    @Transactional
    public DepartmentUserDto updateUserOfDepartment(
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

        log.info("updateDTO: {}", updateDTO);
        userOfDepartmentRepository.updateUserOfCompany(updateDTO.getDepartmentId(), updateDTO.getUserId(),
                updateDTO.getIsManager());
        return userOfDepartmentRepository.findUserOfDepartment(updateDTO.getUserId(), updateDTO.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));

    }

    public void checkUserExistInDepartment(String userId, String departmentId) {
        long count = userOfDepartmentRepository.checkExistUserDepartment(departmentId, userId);
        if (count == 0) {
            throw new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED);
        }
    }

    @Transactional
    public DepartmentUserDto createUserOfDepartment(UserDepartmentCreate userOfDepartmentCreate) {

        long count = userOfDepartmentRepository.checkExistUserDepartment(userOfDepartmentCreate.getDepartmentId(),
                userOfDepartmentCreate.getUserId());
        if (count > 0) {
            throw new AppException(ErrorCode.USER_OF_DEPARTMENT_EXISTED);
        }
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
        if (userOfDepartmentCreate.getIsManager()) {
            if (department.getParentDepartmentId() != null) {
                this.checkManagerOfDepartment(department.getParentDepartmentId(),
                        authoticationService.GetUserIdByToken());
            } else {
                companyService.checkOwnCompany(authoticationService.getUserByToken(), department.getCompanyId());
            }
        }

        userOfDepartmentRepository.insertUserToDepartment(userOfDepartmentCreate.getDepartmentId(),
                userOfDepartmentCreate.getUserId(), userOfDepartmentCreate.getIsManager());

        return userOfDepartmentRepository
                .findUserOfDepartment(userOfDepartmentCreate.getUserId(), userOfDepartmentCreate.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_EXISTED));

    }

    public void deleteUserOfDepartmentBy(String userId, String departmentId) {
        this.checkManagerOfDepartment(departmentId, authoticationService.GetUserIdByToken());
        DepartmentUser departmentUser = userOfDepartmentRepository.getEntityDepartmentUser(userId, departmentId)
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

    public DepartmentUserDto findUserAndDepartment(String user_id, String department_id) {
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
