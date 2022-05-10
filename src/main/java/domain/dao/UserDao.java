package domain.dao;

import domain.entity.Role;
import domain.entity.User;
import memoryDB.UserMemoryDB;
import util.CryptoUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static memoryDB.UserMemoryDB.userMemoryDB;

public class UserDao {
    private static volatile UserDao userDao = null;

    public UserDao(){}

    public UserDao getInstance(){
        if(userDao == null){
            synchronized (UserDao.class){
                if(userDao == null){
                    userDao = new UserDao();
                }
            }
        }
        return userDao;
    }

    public User getUserByUserName(String userName){
        ConcurrentHashMap<Long, User> userDB = userMemoryDB;
        for(User user: userDB.values()){
            if(user.getUserName().equals(userName)){
                return user;
            }
        }
        return null;
    }

    public User getUserById(Long id){
        return userMemoryDB.get(id);
    }

    public void deleteUser(Long id){
        ConcurrentHashMap<Long, User> userDB = userMemoryDB;
        userDB.remove(id);
    }

    public void updateUser(User user, List<Role> roles){
        user.updateRoles(roles);
    }

    public Long saveUser(String userName, String password){
        String cryptoPassword = CryptoUtil.md5Encryption(password);
        User user = new User();
        user.setUserName(userName);
        user.setPassword(cryptoPassword);
        user.setCreatedDate(new Date());
        long userId = UserMemoryDB.id.getAndIncrement();
        user.setId(userId);
        userMemoryDB.put(userId, user);
        return userId;
    }

}
