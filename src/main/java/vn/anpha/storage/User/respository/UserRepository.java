package vn.anpha.storage.User.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import vn.anpha.storage.User.Dto.Projection.UserDto;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM users WHERE email=:email ", nativeQuery = true)
    List<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User removeByUserId(String user_id);

    List<User> findByRefreshToken(String refreshToken);

    @Query(value = """
            SELECT u.user_id, u.email, u.full_name, u.phone, u.address ,roles.name as roleName
            FROM users as u, roles
            WHERE email=:email and  u.role_id=roles.role_id  """, nativeQuery = true)
    Optional<UserDto> FindUserInfoByEmail(String email);

    @Query(value = """
            SELECT u.user_id, u.email, u.full_name, u.phone, u.address ,roles.name as roleName
            FROM users as u, roles
            WHERE user_id=:user_id and  u.role_id=roles.role_id  """, nativeQuery = true)
    Optional<UserDto> FindUserById(String user_id);

    @Query(value = "SELECT user_id FROM users WHERE email=:email ", nativeQuery = true)
    String findUserIdByEmail(String email);

    @Modifying

    @Query(value = """
            INSERT INTO users (user_id, email, full_name, password, phone, updated_at,role_id, created_at, address)
            VALUES (:user_id, :#{#user.email}, :#{#user.fullName},:#{#user.password},:#{#user.phone} , CURRENT_TIMESTAMP,:role, CURRENT_TIMESTAMP,:#{#user.address})""", nativeQuery = true)
    void createUser(@Param("user_id") String user_id, @Param("user") CreateUserDto userdto, long role);

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

    @Modifying
    @Query(value = """
                UPDATE users
                SET address = COALESCE(:#{#userUpdateInfo.address}, address),
                    full_name = COALESCE(:#{#userUpdateInfo.fullName}, full_name),
                    phone = COALESCE(:#{#userUpdateInfo.phone}, phone),
                    updated_at = CURRENT_TIMESTAMP
                WHERE email = :email
            """, nativeQuery = true)
    void updateUserByEmail(@Param("email") String email, @Param("userUpdateInfo") UpdateUserDto userUpdateInfo);

}
