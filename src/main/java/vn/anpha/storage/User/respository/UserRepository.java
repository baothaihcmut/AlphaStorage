package vn.anpha.storage.User.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM users WHERE email=:email ", nativeQuery = true)
    List<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User removeByUserId(String id);

    List<User> findByRefreshToken(String refreshToken);

    @Query(value = "SELECT * FROM users WHERE email=:email LIMIT 1", nativeQuery = true)
    UserRepositoryDto findTest(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE id=:id ", nativeQuery = true)
    User FindUserByID(String id);

    @Query(value = "SELECT user_id FROM users WHERE id=:id ", nativeQuery = true)
    String findUserIdByEmail(String id);

    @Modifying

    @Query(value = """
            INSERT INTO users (user_id, email, full_name, password, phone, updated_at,role_id, created_at, address)
            VALUES (:user_id, :#{#user.email}, :#{#user.fullName},:#{#user.password},:#{#user.phone} , CURRENT_TIMESTAMP,:role, CURRENT_TIMESTAMP,:#{#user.address})""", nativeQuery = true)
    User createUser(@Param("user_id") String user_id, @Param("user") CreateUserDto userdto, long role);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO users (user_id, email, full_name, password, updated_at, role_id, created_at)
            VALUES (:user_id, :email, :fullName, :password, CURRENT_TIMESTAMP, :role, CURRENT_TIMESTAMP)
            """, nativeQuery = true)
    void createInitUser(@Param("user_id") String user_id,
            @Param("email") String email,
            @Param("fullName") String fullName,
            @Param("password") String password,
            @Param("role") long role);

}
