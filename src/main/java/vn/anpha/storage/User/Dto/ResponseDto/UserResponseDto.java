package vn.anpha.storage.User.Dto.ResponseDto;

import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.anpha.storage.Role.Entity.Role;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private UUID userId;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "id=" + userId + ", email=" + email + ", fullName=" + fullName + ", address=" + address + ", phone="
                + phone;
    }
}
