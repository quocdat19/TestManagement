package ra.service;

import ra.model.entity.Enums.ERoles;
import ra.model.entity.Role;

public interface RoleService {
    Role findByRoleName(ERoles roleName);
}
