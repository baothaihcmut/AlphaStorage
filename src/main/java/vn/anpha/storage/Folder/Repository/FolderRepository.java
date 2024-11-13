package vn.anpha.storage.Folder.Repository;

import java.util.List;
import java.util.Optional;
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
        public Optional<Folder> findFolderByID(
                        @Param("id") UUID id);

        @Query(value = "SELECT * FROM folders WHERE parent_folder_id=:parent_id", nativeQuery = true)
        public List<Folder> findAllSubFolder(
                        @Param("parent_id") UUID parentId);

        @Query(value = "WITH RECURSIVE folder_path AS (" +
                        "    SELECT folder_id, name AS folder_name, parent_folder_id, name AS path" +
                        "    FROM folders WHERE folder_id = :folder_init_id" +
                        "    UNION ALL" +
                        "    SELECT f.folder_id, f.name AS folder_name, f.parent_folder_id," +
                        "           CONCAT(f.name, '/', fp.path) AS path" + // Prepend current folder name to path
                        "    FROM folders f" +
                        "    JOIN folder_path fp ON f.folder_id = fp.parent_folder_id" +
                        ")" +
                        "SELECT path FROM folder_path WHERE parent_folder_id is NULL", nativeQuery = true)
        public String findFolderPath(@Param("folder_init_id") UUID folderInitId);

}
