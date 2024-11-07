package vn.anpha.storage.User_company.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class getEmployeeRequestDto {
    private UUID company_id;
    private UUID employee_id;
}
