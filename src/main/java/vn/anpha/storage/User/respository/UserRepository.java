package vn.anpha.storage.User.respository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.User.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User removeById(UUID id);

    List<User> findByRefreshToken(String refreshToken);
}
