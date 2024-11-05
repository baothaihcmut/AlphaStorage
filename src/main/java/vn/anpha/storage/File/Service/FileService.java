package vn.anpha.storage.File.Service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.DTO.Response.FileResponse;
import vn.anpha.storage.File.Entity.File;
import vn.anpha.storage.File.Mapper.FileMapper;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.Folder.Entity.Folder;
import vn.anpha.storage.Folder.Repository.FolderRepository;
import vn.anpha.storage.User.Entity.User;

@Service
public class FileService {
    private FileRepository fileRepository;
    private FileMapper fileMapper;
    private AuthoticationService authService;
    private FolderRepository folderRepository;

    public FileResponse createFile(FileCreationRequest dto)
            throws HttpClientErrorException, HttpServerErrorException {
        File file = fileMapper.toFile(dto);
        User user = authService.getUserByToken();
        file.setCreateBy(user);
        Folder folder = folderRepository.findFolderByID(dto.getFolderId());
        if (folder == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Folder not exist.");
        }
        file.setFolder(folder);
        try {
            file = fileRepository.save(file);
            return fileMapper.toFileResponse(file);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error.");
        }

    }

    public FileResponse getFileById(UUID id) {
        try {
            File file = fileRepository.findFileByID(id);
            if (file == null) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "File not found.");
            }
            return fileMapper.toFileResponse(file);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error.");
        }
    }
}
