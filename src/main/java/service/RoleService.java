package service;

import domain.dao.RoleDao;
import domain.entity.Role;
import util.BaseResult;

import static util.CommonUtils.constructBaseResult;

public class RoleService {
    private final RoleDao roleDao = new RoleDao().getInstance();

    public BaseResult createRole(String roleName){
        if(checkParameterInValid(roleName)){
            return constructBaseResult(false, 400);
        }
        if(getRoleByUserName(roleName) != null){
            return constructBaseResult(false, 409);
        }

        Long userId = roleDao.saveRole(roleName);
        return constructBaseResult(true, 201, userId);

    }

    public Role getRoleByUserName(String roleName){
        return roleDao.getRoleByRoleName(roleName);
    }

    public Role getRoleById(Long id){
        return roleDao.getRoleById(id);
    }

    public BaseResult deleteRole(Long id){
        Role role = getRoleById(id);
        if(role == null){
            return constructBaseResult(false, 404);
        }
        roleDao.deleteRole(id);
        return constructBaseResult(true, 204, id);

    }

    private boolean checkParameterInValid(String roleName){
        return roleName == null || roleName.isEmpty();
    }
}
