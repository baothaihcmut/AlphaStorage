package vn.anpha.storage.History.Controller;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.History.Repository.LogUserResponseProjection;
import vn.anpha.storage.History.Service.HistoryService;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.exception.ResponseDto.ApiResponseDto;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryController {

    HistoryService historyService;

    @GetMapping("getHistory")
    public ApiResponseDto<PaginateResponseDto> getHistory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "")String date,
            @RequestParam(defaultValue = "desc")String sort) {

            return ApiResponseDto.<PaginateResponseDto>builder()
                    .result(historyService.getHistory(date,sort,page,size,email,status))
                    .message("success")
                    .build();
    }

}
