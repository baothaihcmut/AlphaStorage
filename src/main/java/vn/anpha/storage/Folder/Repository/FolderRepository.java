package vn.anpha.storage.Folder.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.Folder.Entity.Folder;

@Repository
public interface FolderRepository
                extends CrudRepository<Folder, UUID>, PagingAndSortingRepository<Folder, UUID> {

        @Query(value = "SELECT * FROM folders WHERE folder_id=:id", nativeQuery = true)
        public Folder findFolderByID(
                        @Param("id") UUID id);

}
