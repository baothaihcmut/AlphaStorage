package vn.anpha.storage.User.respository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.Department.Entity.Department;
import vn.anpha.storage.User.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM users WHERE email=:email ", nativeQuery = true)
    List<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User removeByUserId(UUID id);

    List<User> findByRefreshToken(String refreshToken);

    @Query(value = "SELECT * FROM users WHERE email=:email LIMIT 1", nativeQuery = true)
    UserRepositoryDto findTest(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE id=:id ", nativeQuery = true)
    User FindUserByID(String id);

    @Query(value = "SELECT user_id FROM users WHERE id=:id ", nativeQuery = true)
    String findUserIdByEmail(String id);
}
