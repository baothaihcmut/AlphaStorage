package vn.anpha.storage.Tag.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.anpha.storage.Tag.DTO.Projection.TagDTO;
import vn.anpha.storage.Tag.DTO.Request.TagCreationRequest;
import vn.anpha.storage.Tag.Repository.TagCompanyRepository;
import vn.anpha.storage.Tag.Repository.TagRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagCompanyRepository tagCompanyRepository;

    @Transactional
    public List<TagDTO> createTag(List<TagCreationRequest> request) {
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<TagDTO>> tasks = request.stream().map(tag -> (Callable<TagDTO>) () -> {
                tag.setTagId(UUID.randomUUID().toString());
                this.tagRepository.insertTag(tag);
                if (tag.getIsCompanyTag()) {
                    tag.getTagCompany().setCompanyId(tag.getTagId());
                    this.tagCompanyRepository.insertTagCompany(tag.getTagCompany());
                }
                return this.tagRepository.findTagById(tag.getTagId())
                        .orElseThrow(() -> new Exception("Tag not found"));
            }).toList();
            List<Future<TagDTO>> future = executorService.invokeAll(tasks);
            List<TagDTO> result = new ArrayList<>();
            for (Future<TagDTO> f : future) {
                result.add(f.get());
            }
            return result;
        } catch (ExecutionException e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            if (cause instanceof AppException) {
                throw (AppException) cause;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.SERVER_ERROR);
        }
        // if cannot init executor service throw internal server error
        throw new AppException(ErrorCode.SERVER_ERROR);
    }

}
