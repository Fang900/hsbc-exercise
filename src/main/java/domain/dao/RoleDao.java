package domain.dao;

import domain.entity.Role;
import memoryDB.RoleMemoryDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static memoryDB.RoleMemoryDB.roleMemoryDB;

public class RoleDao {
    private static volatile RoleDao roleDao = null;

    public RoleDao(){}

    public RoleDao getInstance(){
        if(roleDao == null){
            synchronized (RoleDao.class){
                if(roleDao == null){
                    roleDao = new RoleDao();
                }
            }
        }
        return roleDao;
    }

    public Role getRoleByRoleName(String roleName){
        HashMap<Long, Role> roleDB = roleMemoryDB;
        for(Role role: roleDB.values()){
            if(role.getRoleName().equals(roleName)){
                return role;
            }
        }
        return null;
    }

    public Role getRoleById(Long id){
        return roleMemoryDB.get(id);
    }

    public List<Role> getRoleListByIds(List<Long> roleIds){
        ArrayList<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> {
            if(roleMemoryDB.containsKey(roleId)){
                roles.add(roleMemoryDB.get(roleId));
            }
        });
        return roles;
    }

    public void deleteRole(Long id){
        HashMap<Long, Role> roleDB = roleMemoryDB;
        roleDB.remove(id);
    }

    public Long saveRole(String roleName){
        Role role = new Role();
        role.setRoleName(roleName);
        RoleMemoryDB.id++;
        long roleId = RoleMemoryDB.id;
        role.setId(roleId);
        role.setCreatedDate(new Date());
        roleMemoryDB.put(roleId, role);
        return roleId;
    }
}
