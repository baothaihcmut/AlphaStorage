package vn.anpha.storage.Detail.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.Detail.Entity.DetailUser;
import vn.anpha.storage.Role.Entity.Role;

@Repository
public interface DetailRepository extends JpaRepository<DetailUser, UUID> {

}
