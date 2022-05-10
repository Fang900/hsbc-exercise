package service;

import memoryDB.RoleMemoryDB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.BaseResult;

public class RoleServiceTest {
    RoleService roleService;

    @Before
    public void init(){
        roleService = new RoleService();
        RoleMemoryDB.roleMemoryDB.clear();
        RoleMemoryDB.id = 0;
    }

    @Test
    public void createUserTestWithInvalidParam(){
        String roleName = "";
        BaseResult result = roleService.createRole(roleName);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(400, result.getCode());
        Assert.assertEquals("the user or password can not be empty", result.getErrorMessage());
    }

    @Test
    public void createUserTestWithUserExist(){
        String roleName = "test";
        roleService.createRole(roleName);
        BaseResult result = roleService.createRole(roleName);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("the user has already existed", result.getErrorMessage());
    }

    @Test
    public void createUserSuccessTest(){
        String roleName = "test";
        BaseResult result = roleService.createRole(roleName);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(201, result.getCode());
        Assert.assertEquals(1L, result.getData());
    }

    @Test
    public void deleteRoleSuccessTest(){
        String roleName = "test";
        BaseResult result = roleService.createRole(roleName);
        Long roleId = (Long) result.getData();
        BaseResult baseResult = roleService.deleteRole(roleId);
        Assert.assertTrue(baseResult.isSuccess());
        Assert.assertEquals(204, baseResult.getCode());
    }

    @Test
    public void deleteRoleFailTest(){
        String roleName = "test";
        BaseResult result = roleService.createRole(roleName);
        Long roleId = (Long) result.getData();
        BaseResult baseResult = roleService.deleteRole(5L);
        Assert.assertFalse(baseResult.isSuccess());
        Assert.assertEquals(404, baseResult.getCode());
    }
}
