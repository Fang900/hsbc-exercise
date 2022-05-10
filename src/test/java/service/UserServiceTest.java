package service;

import domain.entity.Role;
import domain.entity.User;
import memoryDB.RoleMemoryDB;
import memoryDB.UserMemoryDB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.BaseResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class UserServiceTest {

    UserService userService;
    RoleService roleService;

    @Before
    public void init(){
        userService = new UserService();
        roleService = new RoleService();
        UserMemoryDB.userMemoryDB.clear();
        RoleMemoryDB.roleMemoryDB.clear();
        UserMemoryDB.id = new AtomicLong(0);
        RoleMemoryDB.id = 0;
    }

    @Test
    public void createUserTestWithInvalidParam(){
        String userName = "test";
        BaseResult result = userService.createUser(userName, "");
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(400, result.getCode());
        Assert.assertEquals("the user or password can not be empty", result.getErrorMessage());
    }

    @Test
    public void createUserTestWithUserExist(){
        String userName = "test-user";
        String password = "test-password";
        userService.createUser(userName, password);

        BaseResult result = userService.createUser(userName, password);
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals("the user has already existed", result.getErrorMessage());
    }

    @Test
    public void createUserTestSuccess(){
        String userName = "test-user";
        String password = "test-password";

        BaseResult result = userService.createUser(userName, password);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(201, result.getCode());
        Assert.assertEquals(0L, result.getData());
    }

    @Test
    public void deleteUserTestSuccess(){
        String userName = "test-user";
        String password = "test-password";

        BaseResult result = userService.createUser(userName, password);
        Long userId = (Long) result.getData();
        BaseResult deleteResult = userService.deleteUser(userId);
        Assert.assertTrue(deleteResult.isSuccess());
        Assert.assertEquals(204, deleteResult.getCode());
    }

    @Test
    public void deleteUserTestFail(){
        String userName = "test-user";
        String password = "test-password";

        userService.createUser(userName, password);
        BaseResult deleteResult = userService.deleteUser(4L);
        Assert.assertFalse(deleteResult.isSuccess());
        Assert.assertEquals(404, deleteResult.getCode());
    }

    @Test
    public void authenticateUserFailTest(){
        String userName = "test-user";
        String password = "test-password";

        userService.createUser(userName, password);
        BaseResult result = userService.authenticateUser(userName, "wrong");
        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(401, result.getCode());
        Assert.assertEquals("authenticate error: the user or password is incorrect", result.getErrorMessage());
    }

    @Test
    public void authenticateUserSuccessTest(){
        String userName = "test-user";
        String password = "test-password";
        userService.createUser(userName, password);
        BaseResult result = userService.authenticateUser(userName, password);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(201, result.getCode());
    }

    @Test
    public void visitByToken(){
        String userName = "test-user";
        String password = "test-password";
        userService.createUser(userName, password);
        BaseResult baseResult = userService.authenticateUser(userName, password);
        String token = (String) baseResult.getData();
        BaseResult visitByTokenResult = userService.visitByToken(userName, token);
        Assert.assertTrue(visitByTokenResult.isSuccess());
        Assert.assertEquals(204, visitByTokenResult.getCode());
    }

    @Test
    public void visitByTokenFailBecauseOfTokenExpired(){
        String userName = "test-user";
        String password = "test-password";
        userService.createUser(userName, password);
        BaseResult baseResult = userService.authenticateUser(userName, password);
        User user = userService.getUserByUserName(userName);
        user.setTokenExpireTime(new Date().getTime() - 60 * 60 * 1000);
        String token = (String) baseResult.getData();
        BaseResult visitByTokenResult = userService.visitByToken(userName, token);
        Assert.assertFalse(visitByTokenResult.isSuccess());
        Assert.assertEquals(403, visitByTokenResult.getCode());
    }

    @Test
    public void addRoleToUserTest(){
        String userName = "test-user";
        String password = "test-password";
        BaseResult userResult = userService.createUser(userName, password);
        Long userId = (Long) userResult.getData();
        String roleName1 = "test-role1";
        String roleName2 = "test-role2";
        BaseResult role1 = roleService.createRole(roleName1);
        BaseResult role2 = roleService.createRole(roleName2);
        Long role1Id = (Long) role1.getData();
        Long role2Id = (Long) role2.getData();
        List<Long> roleIds = new ArrayList<>(Arrays.asList(role1Id, role2Id));
        BaseResult addRoleToUserResult = userService.addRoleToUser(userId, roleIds);
        User user = userService.getUserById(userId);
        Assert.assertTrue(addRoleToUserResult.isSuccess());
        Assert.assertEquals(201, addRoleToUserResult.getCode());
        Assert.assertEquals(2, user.getRole().size());

    }

    @Test
    public void getRoleTest(){
        String userName = "test-user";
        String password = "test-password";
        BaseResult userResult = userService.createUser(userName, password);
        Long userId = (Long) userResult.getData();
        String roleName1 = "test-role1";
        String roleName2 = "test-role2";
        BaseResult role1 = roleService.createRole(roleName1);
        BaseResult role2 = roleService.createRole(roleName2);
        Long role1Id = (Long) role1.getData();
        Long role2Id = (Long) role2.getData();
        List<Long> roleIds = new ArrayList<>(Arrays.asList(role1Id, role2Id));
        userService.addRoleToUser(userId, roleIds);
        BaseResult authenticateResult = userService.authenticateUser(userName, password);
        String token = (String) authenticateResult.getData();
        BaseResult allRoles = userService.getAllRoles(userId, token);
        Assert.assertTrue(allRoles.isSuccess());
        Assert.assertEquals(200, allRoles.getCode());
        List<Role> roles = new ArrayList<>();
        if(allRoles.getData() instanceof ArrayList<?>){
            ArrayList<?> list = (ArrayList<?>)allRoles.getData();
            for(Object obj: list){
                if(obj instanceof Role){
                    roles.add((Role) obj);
                }
            }
        }
        Assert.assertEquals(2, roles.size());

    }

    @Test
    public void checkRoleTest(){
        String userName = "test-user";
        String password = "test-password";
        BaseResult userResult = userService.createUser(userName, password);
        Long userId = (Long) userResult.getData();
        String roleName1 = "test-role1";
        String roleName2 = "test-role2";
        BaseResult role1 = roleService.createRole(roleName1);
        BaseResult role2 = roleService.createRole(roleName2);
        Long role1Id = (Long) role1.getData();
        Long role2Id = (Long) role2.getData();
        List<Long> roleIds = new ArrayList<>(Arrays.asList(role1Id, role2Id));
        userService.addRoleToUser(userId, roleIds);
        BaseResult authenticateResult = userService.authenticateUser(userName, password);
        String token = (String) authenticateResult.getData();
        BaseResult allRoles = userService.getAllRoles(userId, token);
        Assert.assertTrue(allRoles.isSuccess());
        Assert.assertEquals(200, allRoles.getCode());


        BaseResult successCheck = userService.checkRole(userId, role1Id, token);
        Assert.assertTrue(successCheck.isSuccess());
        Assert.assertEquals(204, successCheck.getCode());
        BaseResult failCheck = userService.checkRole(userId, 1111L, token);
        Assert.assertFalse(failCheck.isSuccess());
        Assert.assertEquals(416, failCheck.getCode());
        Assert.assertEquals("the user does not have the role", failCheck.getErrorMessage());

    }

//
//
//    public BaseResult checkRole(Long userId, Long roleId, String token){
//        User user = getUserById(userId);
//        if(verifyToken(user, token)){
//            return constructBaseResult(false, 403);
//        }
//        List<Long> roleIds = user.getRole().stream().map(Role::getId).collect(Collectors.toList());
//        if(roleIds.contains(roleId)){
//            return constructBaseResult(true, 204);
//        }
//        return constructBaseResult(false, 416);
//    }
//
//    public BaseResult getAllRoles(Long userId, String token){
//        User user = getUserById(userId);
//        if(verifyToken(user, token)){
//            return constructBaseResult(false, 403);
//        }
//        return constructBaseResult(true, 200, user.getRole());
//    }
}
