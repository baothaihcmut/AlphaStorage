package vn.anpha.storage.File.Controller;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final IFileStructureService fileStructureService;

    @PostMapping("/create")
    @PreAuthorize("@permissionFileService.hasCreatePermission(#dto.departmentId)")
    public ApiResponseDto<FileDetailUploadLinkDTO> createFile(@RequestBody @Valid FileCreationDTO dto)
            throws Exception {
        return ApiResponseDto.<FileDetailUploadLinkDTO>builder().success(true).message("Create file sucess")
                .result(this.fileStructureService.createFile(dto)).build();
    }

    @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
    @PatchMapping("/updateInfo/{fileId}")
    public ApiResponseDto<FileDetailDTO> updateFileInfo(@PathVariable("fileId") UUID fileId,
            @RequestBody @Valid FileUpdateInfoDTO dto) {
        return ApiResponseDto.<FileDetailDTO>builder().success(true).message("Update file infomation success")
                .result(this.fileStructureService.updateFileInfo(fileId, dto)).build();
    }

    @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
    @PatchMapping("/softDelete/{fileId}")
    public ApiResponseDto<Object> softDeleteFile(@PathVariable("fileId") UUID fileId) {
        return ApiResponseDto.<Object>builder().success(false).message("Soft delete file sucess").result(null).build();
    }

    @PreAuthorize("@permissionFileService.hasPermissionManager(#fileId)")
    @PatchMapping("/recover/{fileId}")
    public ApiResponseDto<FileDetailDTO> recoverFile(@PathVariable("fileId") UUID fileId,
            @RequestBody @Valid RecoverFileDTO recoverFileDTO) {
        return ApiResponseDto.<FileDetailDTO>builder().success(true).message("Recover file success")
                .result(this.fileStructureService.recoverFile(fileId, recoverFileDTO)).build();
    }

}
