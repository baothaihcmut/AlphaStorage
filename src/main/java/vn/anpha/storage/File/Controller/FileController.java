package vn.anpha.storage.File.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File.Interface.IWriteFilePermissionService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final IFileStructureService fileStructureService;
    private final IWriteFilePermissionService writeFilePermissionService;

    @PreAuthorize("@writeFilePermissionService.hasPermission(#fileId)")
    @GetMapping("/test/{fileId}")
    public ApiResponseDto<Object> test(@PathVariable("fileId") String fileId) {
        return ApiResponseDto.builder().result(fileId).build();
    }
}
