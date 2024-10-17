package vn.anpha.storage.Role.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.anpha.storage.Role.Entity.Role;
import vn.anpha.storage.Role.Repository.RoleRepository;
import vn.anpha.storage.exception.AppException;
import vn.anpha.storage.exception.ErrorCode;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role FindByName(String Name) {
        List<Role> roles = roleRepository.findByName(Name);
        if (roles.isEmpty()) {
            throw new RuntimeException("Role not found");
        } else {
            return roles.get(0);
        }
    }

    public Role CreateAdminRole() {
        if (roleRepository.findByName("Admin").isEmpty()) {
            Role role = new Role();
            role.setName("Admin");
            role.setDescription("Admin role");
            return roleRepository.save(role);
        } else {
            throw new AppException(ErrorCode.Role_Is_EXIST);
        }
    }

    public Role CreateUserRole() {
        if (roleRepository.findByName("User").isEmpty()) {
            Role role = new Role();
            role.setName("User");
            role.setDescription("User role");
            return roleRepository.save(role);
        } else {
            throw new AppException(ErrorCode.Role_Is_EXIST);
        }
    }

    public Role CreateOwnCompanyRole() {
        if (roleRepository.findByName("OwnCompany").isEmpty()) {
            Role role = new Role();
            role.setName("OwnCompany");
            role.setDescription("OwnCompany role");
            return roleRepository.save(role);
        } else {
            throw new AppException(ErrorCode.Role_Is_EXIST);
        }
    }
}
