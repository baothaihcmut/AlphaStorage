package vn.anpha.storage.User.Dto.ResponseDto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.anpha.storage.exception.ResponseDto.MetaPaginate;

@Getter
@Setter
public class PaginateResponseDto<T> {
    private MetaPaginate metaPaginate;
    private List<T> data;

}
