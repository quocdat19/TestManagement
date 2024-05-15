package ra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Enums.ERoles;
import ra.model.entity.Role;
import ra.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findByRoleName(ERoles roleName) {
        return roleRepository.findByRole ( roleName );
    }
}
