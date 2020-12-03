
package com.ddcoding.framework.persistent.shiro;

import com.ddcoding.framework.persistent.BaseDao;
import com.ddcoding.framework.persistent.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
public class HibernateRealm extends AuthorizingRealm {

    @Autowired
    private BaseDao baseDao;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) super.getAvailablePrincipal(principals);
        User param = new User();
        param.setUserName(username);
        return baseDao.getUnique(User.class, param);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        User param = new User();
        param.setUserName(username);
        return baseDao.getUnique(User.class, param);
    }

}
