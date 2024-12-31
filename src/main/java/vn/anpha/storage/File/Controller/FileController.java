package vn.anpha.storage.File.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.File.DTO.Projection.FileDetailDTO;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Request.AnnounceUploadDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileDTO;
import vn.anpha.storage.File.DTO.Response.FileDetailUploadLinkDTO;
import vn.anpha.storage.File.DTO.Response.FileMetaDataLinkDTO;
import vn.anpha.storage.File.Interface.IFileService;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
        private final IFileStructureService fileStructureService;
        private final IFileService fileService;

        @PostMapping("/upload")
        @PreAuthorize("@permissionFileService.hasCreatePermission(#dto.departmentId)")
        public ApiResponseDto<FileDetailUploadLinkDTO> uploadFile(@RequestBody @Valid FileCreationDTO dto)
                        throws Exception {
                return ApiResponseDto.<FileDetailUploadLinkDTO>builder().success(true).message("Create file sucess")
                                .result(this.fileService.uploadFile(dto)).build();
        }

        @PatchMapping("/update/{fileId}")
        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        public ApiResponseDto<FileMetaDataLinkDTO> updateFile(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid UpdateFileDTO updateFileDTO) throws Exception {
                return ApiResponseDto.<FileMetaDataLinkDTO>builder().success(true).message("Update file success")
                                .result(this.fileService.updateFile(fileId, updateFileDTO)).build();
        }

        @PostMapping("/announceUpload/{fileId}")
        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        public ApiResponseDto<FileMetaDataDTO> announceUpload(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid AnnounceUploadDTO annouceUploadDTO) {
                return ApiResponseDto.<FileMetaDataDTO>builder().success(true).message("Announce upload success")
                                .result(this.fileService.announceUploadFile(fileId, annouceUploadDTO)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @GetMapping("/download/{fileId}")
        public ApiResponseDto<FileMetaDataLinkDTO> downloadFile(@PathVariable("fileId") String fileId)
                        throws Exception {
                return ApiResponseDto.<FileMetaDataLinkDTO>builder().success(true)
                                .message("Get metadata for download success")
                                .result(this.fileService.downloadFile(fileId)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @PatchMapping("/updateInfo/{fileId}")
        public ApiResponseDto<FileDetailDTO> updateFileInfo(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid FileUpdateInfoDTO dto) {
                return ApiResponseDto.<FileDetailDTO>builder().success(true).message("Update file infomation success")
                                .result(this.fileStructureService.updateFileInfo(fileId, dto)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @PatchMapping("/softDelete/{fileId}")
        public ApiResponseDto<Object> softDeleteFile(@PathVariable("fileId") String fileId) {
                this.fileStructureService.deleteFileSoft(fileId);
                return ApiResponseDto.<Object>builder().success(false).message("Soft delete file sucess").result(null)
                                .build();
        }

        @PreAuthorize("@permissionFileService.hasRecoverPermission(#fileId)")
        @PatchMapping("/recover/{fileId}")
        public ApiResponseDto<FileDetailDTO> recoverFile(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid RecoverFileDTO recoverFileDTO) {
                return ApiResponseDto.<FileDetailDTO>builder().success(true).message("Recover file success")
                                .result(this.fileStructureService.recoverFile(fileId, recoverFileDTO)).build();
        }

}
