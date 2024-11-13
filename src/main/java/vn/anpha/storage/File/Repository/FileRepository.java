package vn.anpha.storage.File.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.File.Entity.File;

@Repository
public interface FileRepository extends CrudRepository<File, UUID>, PagingAndSortingRepository<File, UUID> {

        @Query(value = "SELECT * from files WHERE file_id=:id AND is_deleted=:is_deleted", nativeQuery = true)
        public Optional<File> findFileByID(
                        @Param("id") UUID id,
                        @Param("is_deleted") Boolean isDeleted);

        @Query(value = "SELECT * FROM files WHERE folder_id=:folder_id AND is_deleted=:is_deleted", nativeQuery = true)
        public List<File> findAllFileInFolder(
                        @Param("folder_id") UUID folderId,
                        @Param("is_deleted") Boolean isDeleted);

}
