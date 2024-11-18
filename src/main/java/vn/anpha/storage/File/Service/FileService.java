package vn.anpha.storage.File.Service;

import java.math.BigInteger;
import java.util.AbstractMap.SimpleEntry;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Company.Entity.Company;
import vn.anpha.storage.Company.Service.CompanyService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.Detail.Service.DetailService;
import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.File.Mapper.FileMapper;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Storage.service.StorageService;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.Repository.UserOfDepartmentRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final AuthoticationService authService;
    private final DepartmentRepository departmentRepository;
    private final UserOfDepartmentRepository userOfDepartmentRepository;
    private final CompanyService companyService;
    private final StorageService storageService;

    private final DetailService detailService;

    public String processCompanyFile(User user, File file) {
        Department department = this.departmentRepository
                .findDepartmentById(file.getDepartment().getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
        this.userOfDepartmentRepository.findUserOfDepartment(user.getUserId(), department.getDepartmentId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
        file.setDepartment(department);
        // check size
        Company company = this.companyService.updateCompanySize(department.getCompany().getCompanyId(),
                BigInteger.valueOf(file.getFileDetail().getSize().longValue()));
        return company.getName();
    }

    public String processPersonalFile(User user, File file) {
        this.detailService.checkSizeAndUpdateSize(user, file.getFileDetail().getSize());
        return user.getEmail();
    }

    @Transactional
    public FileResponse createFile(FileCreationRequest dto) throws Exception {
        File file = this.fileMapper.toFile(dto);
        User user = this.authService.getUserByToken();
        // set owner of file
        file.setCreateBy(user);
        String bucketName;
        // check permission and file
        if (!file.getIsPersional()) {
            bucketName = this.processCompanyFile(user, file);
        } else {
            bucketName = this.processPersonalFile(user, file);
        }
        // insert tags file

        // upload file
        SimpleEntry<String, String> uploadRes = this.storageService.getPresignUrlForPut(bucketName, file.getName(), 3);
        file.getFileDetail().setLink(uploadRes.getKey());
        // insert file and file detail
        file.getFileDetail().setFile(file);
        file = this.fileRepository.save(file);
        file.getFileDetail().setLink(uploadRes.getValue());
        return this.fileMapper.toFileResponse(file);
    }

}