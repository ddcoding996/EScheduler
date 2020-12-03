package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.common.helper.ObjectHelper;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.User;
import com.ddcoding.framework.persistent.shiro.HashHelper;
import com.ddcoding.framework.service.ServiceException;
import com.ddcoding.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BaseDao baseDao;

    @Override
    public void updatePassword(String username, String password) {
        User param = new User();
        param.setUserName(username);
        User userInDb = baseDao.getUnique(User.class, param);
        if (ObjectHelper.isEmpty(userInDb)) {
            throw new ServiceException("can't find user.");
        }
        userInDb.setUserPassword(HashHelper.getHashedPassword(password, userInDb.getPasswordSalt()));
        baseDao.update(userInDb);
    }

    @Override
    public void saveUser(User user) {
        User param = new User();
        param.setUserName(user.getUserName());
        User userInDb = baseDao.getUnique(User.class, param);
        if (!ObjectHelper.isEmpty(userInDb)) {
            throw new ServiceException("username exists.");
        }
        baseDao.save(user);
    }

}
