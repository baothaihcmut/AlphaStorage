package vn.anpha.storage.File_Tag.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.Department.Repository.DepartmentRepository;
import vn.anpha.storage.File.DTO.Projection.FileDTO;
import vn.anpha.storage.File.Repository.FileRepository;
import vn.anpha.storage.File_Tag.DTO.Projection.FileTagDTO;
import vn.anpha.storage.File_Tag.DTO.Request.FileTagCreationRequest;
import vn.anpha.storage.File_Tag.Repository.FileTagRepository;
import vn.anpha.storage.Tag.DTO.Projection.TagCompanyDTO;
import vn.anpha.storage.Tag.DTO.Projection.TagDTO;
import vn.anpha.storage.Tag.Repository.TagCompanyRepository;
import vn.anpha.storage.Tag.Repository.TagRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class FileTagService {
    private final FileTagRepository fileTagRepository;
    private final TagRepository tagRepository;
    private final TagCompanyRepository tagCompanyRepository;

    private final DepartmentRepository departmentRepository;
    private final FileRepository fileRepository;

    @Transactional
    public void insertFileTag(FileTagCreationRequest fileTag) {
        // check if tag is company
        TagDTO tag = this.tagRepository.findTagById(fileTag.getTagId())
                .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXIST));
        if (tag.getIsCompanyTag()) {
            // if tag is company
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                Future<TagCompanyDTO> tagTask = executor.submit(() -> {
                    return this.tagCompanyRepository.findTagCompanyById(fileTag.getTagId())
                            .orElseThrow(() -> new AppException(ErrorCode.TAG_NOT_EXIST));
                });
                Future<DepartmentDTO> departmentTask = executor.submit(() -> {
                    FileDTO file = this.fileRepository.findFileById(fileTag.getFileId(), false)
                            .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXIST));
                    return this.departmentRepository.findDepartmentById(file.getDepartmentId())
                            .orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED));
                });

                if (!tagTask.get().getCompanyId().equals(departmentTask.get().getCompanyId())) {
                    throw new AppException(ErrorCode.TAG_COMPANY_MISMATCH);
                }
                this.fileTagRepository.insertFileTag(fileTag);
            } catch (ExecutionException e) {
                e.printStackTrace();
                Throwable cause = e.getCause();
                if (cause instanceof AppException) {
                    throw (AppException) cause;
                } else {
                    throw new AppException(ErrorCode.SERVER_ERROR);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new AppException(ErrorCode.SERVER_ERROR);
            }
        }
    }

    @Transactional
    public void insertFileTagBulk(List<FileTagCreationRequest> fileTags) {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<Void>> tasks = fileTags.stream().map(fileTag -> (Callable<Void>) () -> {
                this.insertFileTag(fileTag);
                return null;
            }).toList();
            List<Future<Void>> futures = executor.invokeAll(tasks);
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause instanceof AppException) {
                throw (AppException) cause;
            } else {
                throw new AppException(ErrorCode.SERVER_ERROR);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
    }

    @Transactional
    public void deleteFileTag(String fileId, String tagId) {
        this.fileTagRepository.deleteFileTag(fileId, tagId);
    }

    public List<FileTagDTO> getAllTagOfFile(String fileId) {
        return this.fileTagRepository.findFileTagByFileId(fileId);
    }
}
