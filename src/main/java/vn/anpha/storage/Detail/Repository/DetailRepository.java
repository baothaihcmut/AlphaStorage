package vn.anpha.storage.Detail.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.Detail.Entity.DetailUser;

@Repository
public interface DetailRepository extends JpaRepository<DetailUser, UUID> {
    // @Query(value = "SELECT * from detailUsers WHERE id=:user_id LIMIT 1")
    // Optional<DetailUser> findDetailOfUser(@Param("user_id") UUID userId);
}
