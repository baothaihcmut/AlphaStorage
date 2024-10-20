package vn.anpha.storage.User.Dto.ResponseDto;

import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {

    private UUID id;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private String Role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return "id=" + id + ", email=" + email + ", fullName=" + fullName + ", address=" + address + ", phone=" + phone;
    }
}
