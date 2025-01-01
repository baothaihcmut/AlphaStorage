package vn.anpha.storage.User.respository;

import java.time.LocalDateTime;
import java.util.UUID;

import vn.anpha.storage.Role.Entity.Role;

public interface UserRepositoryDto {
    String getUserId();

    String getEmail();

    String getPassword();

    String getFullName();

    String getAddress();

    String getPhone();

    String getRefreshToken();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

}
