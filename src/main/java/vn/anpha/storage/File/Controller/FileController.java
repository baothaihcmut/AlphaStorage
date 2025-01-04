package vn.anpha.storage.File.Controller;

import java.util.List;

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
import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.DTO.Projection.FileMetaDataDTO;
import vn.anpha.storage.File.DTO.Projection.StorageDetailDTO;
import vn.anpha.storage.File.DTO.Request.AnnounceUploadDTO;
import vn.anpha.storage.File.DTO.Request.FileCreationDTO;
import vn.anpha.storage.File.DTO.Request.FileUpdateInfoDTO;
import vn.anpha.storage.File.DTO.Request.RecoverFileDTO;
import vn.anpha.storage.File.DTO.Request.UpdateFileContentDTO;
import vn.anpha.storage.File.DTO.Response.CreateFileResponse;
import vn.anpha.storage.File.DTO.Response.UpdateFileContentResponse;
import vn.anpha.storage.File.Interface.IFileService;
import vn.anpha.storage.File.Interface.IFileStructureService;
import vn.anpha.storage.File_Tag.DTO.Request.FileTagCreationRequest;
import vn.anpha.storage.File_Tag.Service.FileTagService;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
        private final IFileStructureService fileStructureService;
        private final IFileService fileService;
        private final FileTagService fileTagService;

        @PostMapping("")
        @PreAuthorize("@permissionFileService.hasDepartmentPermission(#dto.departmentId)")
        public ApiResponseDto<CreateFileResponse> createFile(@RequestBody @Valid FileCreationDTO dto)
                        throws Exception {
                return ApiResponseDto.<CreateFileResponse>builder().success(true).message("Create file sucess")
                                .result(this.fileService.createFile(dto)).build();
        }

        @PatchMapping("/{fileId}/content")
        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        public ApiResponseDto<UpdateFileContentResponse> updateFile(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid UpdateFileContentDTO updateFileDTO) throws Exception {
                return ApiResponseDto.<UpdateFileContentResponse>builder().success(true).message("Update file success")
                                .result(this.fileService.updateFileContent(fileId, updateFileDTO)).build();
        }

        @PostMapping("/{fileId}/announce-upload")
        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        public ApiResponseDto<FileMetaDataDTO> announceUpload(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid AnnounceUploadDTO annouceUploadDTO) {
                return ApiResponseDto.<FileMetaDataDTO>builder().success(true).message("Announce upload success")
                                .result(this.fileService.announceUploadFile(fileId, annouceUploadDTO)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @GetMapping("/{fileId}/content")
        public ApiResponseDto<StorageDetailDTO> downloadFile(@PathVariable("fileId") String fileId)
                        throws Exception {
                return ApiResponseDto.<StorageDetailDTO>builder().success(true)
                                .message("Get metadata for download success")
                                .result(this.fileService.downloadFile(fileId)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @PatchMapping("/{fileId}/detail")
        public ApiResponseDto<FileDTO> updateFileInfo(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid FileUpdateInfoDTO dto) {
                return ApiResponseDto.<FileDTO>builder().success(true).message("Update file infomation success")
                                .result(this.fileStructureService.updateFileDetail(fileId, dto)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @PatchMapping("/{fileId}/soft-delete")
        public ApiResponseDto<FileDTO> softDeleteFile(@PathVariable("fileId") String fileId) {
                this.fileStructureService.deleteFileSoft(fileId);
                return ApiResponseDto.<FileDTO>builder().success(false).message("Soft delete file sucess").result(null)
                                .build();
        }

        @PreAuthorize("@permissionFileService.hasRecoverPermission(#fileId)")
        @PatchMapping("/{fileId}/recover")
        public ApiResponseDto<FileDTO> recoverFile(@PathVariable("fileId") String fileId,
                        @RequestBody @Valid RecoverFileDTO recoverFileDTO) {
                return ApiResponseDto.<FileDTO>builder().success(true).message("Recover file success")
                                .result(this.fileStructureService.recoverFile(fileId, recoverFileDTO)).build();
        }

        @PreAuthorize("@permissionFileService.hasDepartmentPermission(#departmentId)")
        @GetMapping("/structure/{departmentId}")
        public ApiResponseDto<Object> getFileStructure(@PathVariable("departmentId") String departmentId) {
                return ApiResponseDto.<Object>builder().success(true).message("Get file structure success")
                                .result(this.fileStructureService.getFileStructure(departmentId)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @GetMapping("/{fileId}/detail")
        public ApiResponseDto<FileDTO> getFileDetail(@PathVariable("fileId") String fileId) {
                return ApiResponseDto.<FileDTO>builder().success(true).message("Get file detail success")
                                .result(this.fileService.getFileDetail(fileId)).build();
        }

        @PreAuthorize("@permissionFileService.hasPermission(#fileId)")
        @PostMapping("/{fileId}/tags")
        public ApiResponseDto<Object> addTag(List<FileTagCreationRequest> tags) {
                this.fileTagService.insertFileTagBulk(tags);
                return ApiResponseDto.builder().success(true).message("Add tag success")
                                .result(null).build();
        }

}
