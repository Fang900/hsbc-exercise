package service;

import domain.dao.RoleDao;
import domain.dao.UserDao;
import domain.entity.Role;
import domain.entity.User;
import util.BaseResult;
import util.CryptoUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static util.CommonUtils.constructBaseResult;

public class UserService {

    private final UserDao userDao = new UserDao().getInstance();
    private final RoleDao roleDao = new RoleDao().getInstance();
    private static final long TWO_HOUR = 2 * 60 * 60 * 1000;


    public BaseResult createUser(String userName, String password){
        if(checkParameterInValid(userName, password)){
            return constructBaseResult(false, 400);
        }
        if(getUserByUserName(userName) != null){
            return constructBaseResult(false, 409);
        }

        Long userId = userDao.saveUser(userName, password);
        return constructBaseResult(true, 201, userId);

    }

    public BaseResult addRoleToUser(Long userId, List<Long> roleIds){
        User user = getUserById(userId);
        if(user == null){
            return constructBaseResult(false, 404);
        }
        List<Role> roles = roleDao.getRoleListByIds(roleIds);
        return updateUser(user, roles);
    }

    public BaseResult authenticateUser(String userName, String password){
        if(checkParameterInValid(userName, password)){
            return constructBaseResult(false, 400);
        }
        User user = getUserByUserName(userName);
        if(user == null){
            return constructBaseResult(false, 404);
        }
        String cryptoPassword = CryptoUtil.md5Encryption(password);
        if (cryptoPassword != null && cryptoPassword.equals(user.getPassword())) {
            String token = UUID.randomUUID().toString();
            long expireTime = new Date().getTime() + TWO_HOUR;
            user.setToken(token);
            user.setTokenExpireTime(expireTime);
            return constructBaseResult(true, 201, token);
        }
        return constructBaseResult(false, 401);
    }

    public BaseResult visitByToken(String userName, String token){
        User user = getUserByUserName(userName);
        if(verifyToken(user, token)){
            user.setToken(null);
            user.setTokenExpireTime(0);
            return constructBaseResult(false, 403);
        }
        return constructBaseResult(true, 204);
    }

    public BaseResult checkRole(Long userId, Long roleId, String token){
        User user = getUserById(userId);
        if(verifyToken(user, token)){
            return constructBaseResult(false, 403);
        }
        List<Long> roleIds = user.getRole().stream().map(Role::getId).collect(Collectors.toList());
        if(roleIds.contains(roleId)){
            return constructBaseResult(true, 204);
        }
        return constructBaseResult(false, 416);
    }

    public BaseResult getAllRoles(Long userId, String token){
        User user = getUserById(userId);
        if(verifyToken(user, token)){
            return constructBaseResult(false, 403);
        }
        return constructBaseResult(true, 200, user.getRole());
    }

    public User getUserByUserName(String userName){
        return userDao.getUserByUserName(userName);
    }

    public User getUserById(Long id){
        return userDao.getUserById(id);
    }

    public BaseResult updateUser(User user, List<Role> roles){
        userDao.updateUser(user, roles);
        return constructBaseResult(true, 201, user.getId());
    }

    public BaseResult deleteUser(Long id){
        User user = getUserById(id);
        if(user == null){
            return constructBaseResult(false, 404);
        }
        userDao.deleteUser(id);
        return constructBaseResult(true, 204, id);
    }

    private boolean verifyToken(User user, String token){
        return token.isEmpty() || !user.getToken().equals(token) || new Date().getTime() > user.getTokenExpireTime();
    }

    private boolean checkParameterInValid(String userName, String password){
        return userName == null || userName.isEmpty() || password == null || password.isEmpty();
    }


}
