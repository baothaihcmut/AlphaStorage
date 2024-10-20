package vn.anpha.storage.Detail.Service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.anpha.storage.Detail.Entity.DetailUser;
import vn.anpha.storage.Detail.Repository.DetailRepository;
import vn.anpha.storage.User.Dto.RequestDto.CreateUserDto;
import vn.anpha.storage.User.Entity.User;

@Slf4j
@Service
public class DetailService {

    private final DetailRepository detailrepository;

    public DetailService(DetailRepository detailrepository) {
        this.detailrepository = detailrepository;
    }

    public void createDetail(User user) {

        DetailUser userDetail = new DetailUser();

        userDetail.setUser(user);
        this.detailrepository.save(userDetail);
    }
}
