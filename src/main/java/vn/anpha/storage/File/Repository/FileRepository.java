package vn.anpha.storage.File.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.File.Entity.File;

@Repository
public interface FileRepository extends CrudRepository<File, UUID>, PagingAndSortingRepository<File, UUID> {

    @Query(value = "SELECT * from files WHERE file_id=:id", nativeQuery = true)
    public File findFileByID(
            @Param("id") UUID id);
}
