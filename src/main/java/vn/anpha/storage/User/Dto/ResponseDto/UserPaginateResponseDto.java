package vn.anpha.storage.User.Dto.ResponseDto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.anpha.storage.exception.ResponseDto.MetaPaginate;

@Getter
@Setter
@Builder
public class UserPaginateResponseDto {
    private MetaPaginate metaPaginate;
    private List<UserResponseDto> data;

}
