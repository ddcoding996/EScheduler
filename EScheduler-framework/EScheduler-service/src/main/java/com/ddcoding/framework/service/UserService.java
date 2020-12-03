package com.ddcoding.framework.service;


import com.ddcoding.framework.persistent.entity.User;

/**
 */
public interface UserService {

    void updatePassword(String username, String password);

    void saveUser(User user);

}
