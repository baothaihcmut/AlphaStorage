package vn.anpha.storage.File.Controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.File.DTO.Projection.FileExistProjection;
import vn.anpha.storage.File.DTO.Projection.FileProjection;
import vn.anpha.storage.File.DTO.Request.FileCreationRequest;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File.Interface.IWriteFilePermissionService;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final IFileStructureService fileStructureService;
    private final FileRepository fileRepository;
    private final IWriteFilePermissionService writeFilePermissionService;

    @PostMapping("/test")
    public ApiResponseDto<Object> test(@RequestBody @Valid FileCreationRequest fileCreationRequest) throws Exception {
        FileProjection fileProjection = this.fileStructureService.createFile(fileCreationRequest);
        System.out.println(fileProjection.getFileId());
        return ApiResponseDto.builder().success(true).message("sucess").result(fileProjection.getFileId()).build();
    }

    @GetMapping("/test/{id}")
    public ApiResponseDto<Object> test(@PathVariable("id") UUID id) throws Exception {
        System.out.println(id);
        FileExistProjection fileProjection = this.fileRepository.findFileById(id, false)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
        System.out.println(fileProjection);
        return ApiResponseDto.builder().success(true).message("sucess").result(fileProjection).build();
    }
}
