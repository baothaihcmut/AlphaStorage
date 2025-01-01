package vn.anpha.storage.User.respository;

import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.anpha.storage.User.Dto.RequestDto.UpdateUserDto;
import vn.anpha.storage.User.Entity.User;
import vn.anpha.storage.User_company.Entity.UserOfCompany;

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
                        INSERT INTO users (email, fullname, password, phone, update_at,role_id, created_at, address)
                        VALUES (:#{#user.email}, :#{#user.fullName},:#{#user.password},:#{#user.phone} , CURRENT_TIMESTAMP,:#{#user.role}, CURRENT_TIMESTAMP,:#{#user.address})""", nativeQuery = true)
    User createUser(@Param("User")User user);

    @Modifying
    @Query(value = """
                UPDATE users 
                SET fullname = :#{#user.fullName}, 
                    phone = :#{#user.phone}, 
                    updated_at = CURRENT_TIMESTAMP, 
                    address = :#{#user.address}
                WHERE email = :email
                """, nativeQuery = true)
    User updateUser(@Param("user")UpdateUserDto user, String email);
}
