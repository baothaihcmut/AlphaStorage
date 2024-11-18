package vn.anpha.storage.Detail.Service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Detail.Entity.DetailUser;
import vn.anpha.storage.Detail.Repository.DetailRepository;
import vn.anpha.storage.User.Entity.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetailService {

    private final DetailRepository detailrepository;

    public void createDetail(User user) {

        DetailUser userDetail = new DetailUser();

        userDetail.setUser(user);
        this.detailrepository.save(userDetail);
    }

    @Transactional
    public void checkSizeAndUpdateSize(User user, Integer additionSize) {
        // DetailUser detailUser =
        // this.detailrepository.findDetailOfUser(user.getUserId())
        // .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // if (detailUser.getTotal_size() + additionSize > detailUser.getLimit_size()) {
        // throw new AppException(ErrorCode.PERSONAL_EXEED_LIMIT_SIZE);
        // }
        // detailUser.setTotal_size(detailUser.getTotal_size() + additionSize);
        // this.detailrepository.save(detailUser);
    }

    @Transactional
    public void removeFile(User user, Integer size) {
        // DetailUser detailUser =
        // this.detailrepository.findDetailOfUser(user.getUserId())
        // .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        // detailUser.setTotal_size(detailUser.getTotal_size() - size);
        // this.detailrepository.save(detailUser);

    }
}
