package vn.anpha.storage.exception.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MetaPaginate {
    private int CurrentPage;

    private long TotalItems;
    private int TotalPages;
    private int PageSize;

}