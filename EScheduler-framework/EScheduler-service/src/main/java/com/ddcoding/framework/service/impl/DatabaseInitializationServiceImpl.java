package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.Role;
import com.ddcoding.framework.persistent.entity.User;
import com.ddcoding.framework.persistent.shiro.HashHelper;
import com.ddcoding.framework.service.DatabaseInitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 数据库初始化服务实现类
 *
 */
@Component
public class DatabaseInitializationServiceImpl implements DatabaseInitializationService {

    @Autowired
    private BaseDao baseDao;

    /**
     * 本方法先检查数据库是否有admin为username的用户,如果没有则会插入一条,并且赋予权限.
     */
    @Override
    public void initialize() {
        String adminUsername = "admin";
        String adminPassword = "123456";

        User param = new User();
        param.setUserName(adminUsername);
        User admin = baseDao.getUnique(User.class, param);
        if (admin != null) {
            LoggerHelper.info("database has been initialized.");
            return;
        }
        LoggerHelper.info("begin init database.");
        admin = new User();
        admin.setUserName(adminUsername);
        admin.setUserPassword(HashHelper.getHashedPassword(adminPassword, adminUsername));
        admin.setPasswordSalt(adminUsername);

        Role role = new Role();
        role.setRoleName("ROLE_ADMIN");
        role.setRoleDescription("Administrator");
        baseDao.save(role);

        admin.setRoleList(Arrays.asList(role));
        baseDao.save(admin);

        LoggerHelper.info("init database successfully.");
    }

}
