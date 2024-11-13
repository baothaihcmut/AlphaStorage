package vn.anpha.storage.Folder.Service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.Folder.DTO.Request.FolderCreationRequest;
import vn.anpha.storage.Folder.DTO.Request.FolderUpdateRequest;
import vn.anpha.storage.Folder.DTO.Response.FolderReponse;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.Folder.Mapper.FolderMapper;
import vn.anpha.storage.Folder.Repository.FolderRepository;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_Department.Repository.UserOfDepartmentRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class FolderService {
        private FolderRepository folderRepository;
        private FolderMapper folderMapper;
        private AuthoticationService authService;
        private DepartmentRepository departmentRepository;
        private UserOfDepartmentRepository userOfDepartmentRepository;

        public FolderReponse createFolder(FolderCreationRequest dto) {
                Folder folder = folderMapper.toFolder(dto);

                User user = authService.getUserByToken();
                folder.setPersionalUser(user);
                if (!dto.getIsPersonal()) {
                        Department department = departmentRepository.findDepartmentById(dto.getDepartmentId())
                                        .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                        folder.setDepartment(department);
                        userOfDepartmentRepository.findUserOfDepartment(user.getUserId(), department.getDepartmentId())
                                        .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
                }
                if (dto.getParentFolderId() != null) {
                        Folder parentFolder = folderRepository.findById(dto.getParentFolderId())
                                        .orElseThrow(() -> new AppException(ErrorCode.PARENT_FOLDER_NOT_EXIST));
                        folder.setParentFolder(parentFolder);
                }
                folder = this.folderRepository.save(folder);
                return this.folderMapper.toFolderResponse(folder);
        }

        public FolderReponse updateFolder(UUID folderId, FolderUpdateRequest dto) {
                User user = authService.getUserByToken();
                Folder folder = folderRepository.findById(folderId)
                                .orElseThrow(() -> new AppException(ErrorCode.FOLDER_NOT_EXIST));
                if (!folder.getIsPersonal()) {
                        Department department = departmentRepository
                                        .findDepartmentById(folder.getDepartment().getDepartmentId())
                                        .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                        userOfDepartmentRepository.findUserOfDepartment(user.getUserId(), department.getDepartmentId())
                                        .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
                }
                folder.setName(dto.getName());
                folder.setDescription(dto.getDescription());
                folder.setLimit_size(dto.getLimit_size());
                folder.setTotal_size(dto.getTotal_size());
                folder = this.folderRepository.save(folder);
                return this.folderMapper.toFolderResponse(folder);
        }

        public List<FolderReponse> getAllSubFolder(UUID folderId) {
                User user = authService.getUserByToken();
                Folder folder = folderRepository.findById(folderId)
                                .orElseThrow(() -> new AppException(ErrorCode.FOLDER_NOT_EXIST));
                if (!folder.getIsPersonal()) {
                        Department department = departmentRepository
                                        .findDepartmentById(folder.getDepartment().getDepartmentId())
                                        .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                        userOfDepartmentRepository.findUserOfDepartment(user.getUserId(), department.getDepartmentId())
                                        .orElseThrow(() -> new AppException(ErrorCode.USER_OF_DEPARTMENT_NOT_YOURS));
                }
                return this.folderRepository.findAllSubFolder(folderId).stream()
                                .map(this.folderMapper::toFolderResponse).toList();
        }
}
