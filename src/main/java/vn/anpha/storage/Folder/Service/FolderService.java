package vn.anpha.storage.Folder.Service;

import org.springframework.stereotype.Service;

import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.Folder.Mapper.FolderMapper;
import vn.anpha.storage.Folder.Repository.FolderRepository;

@Service
public class FolderService {
    private FolderRepository folderRepository;
    private FolderMapper folderMapper;
    private AuthoticationService authService;
    private DepartmentRepository departmentRepository;

    // public FolderReponse createFolder(FolderCreationRequest dto) {
    // Folder folder = folderMapper.toFolder(dto);
    // if (dto.getIsPersonal()) {
    // User user = authService.getUserByToken();
    // folder.setPersionalUser(user);
    // } else {
    // Department department =
    // departmentRepository.findDepartmentById(dto.getDepartmentId());
    // folder.setDepartment(department);
    // }

    // }
}
