package vn.anpha.storage.History.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Auth.Service.AuthoticationService;
import vn.anpha.storage.Department.DTO.projection.DepartmentDTO;
import vn.anpha.storage.History.Repository.LogUserRepository;
import vn.anpha.storage.History.Repository.LogUserResponseProjection;
import vn.anpha.storage.User.Dto.ResponseDto.PaginateResponseDto;
import vn.anpha.storage.User.respository.UserRepository;
import vn.anpha.storage.exception.ResponseDto.MetaPaginate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HistoryService {
    AuthoticationService authoticationService;
    UserRepository userRepository;
    LogUserRepository logUserRepository;

    public PaginateResponseDto<LogUserResponseProjection> getHistory(
            String date, String sort, Integer page, Integer size, String email, String status) {

        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Sort sortOrder = Sort.by(direction, sortParams[0]);

        Pageable pageable = PageRequest.of(page - 1, size, sortOrder);

        Page<LogUserResponseProjection> pageLog = this.logUserRepository.findFilter(
                pageable, email, status, date);

        List<LogUserResponseProjection> pageLogUser = pageLog.getContent();

        MetaPaginate pageMeta = MetaPaginate.builder()
                .CurrentPage(pageLog.getNumber() + 1)
                .PageSize(pageLog.getSize())
                .TotalItems(pageLog.getTotalElements())
                .TotalPages(pageLog.getTotalPages())
                .build();

        PaginateResponseDto<LogUserResponseProjection> responseDto = new PaginateResponseDto<>();
        responseDto.setData(pageLogUser);
        responseDto.setMetaPaginate(pageMeta);
        return responseDto;
    }



}
