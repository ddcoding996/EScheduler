
package com.ddcoding.framework.persistent.entity;

import com.ddcoding.framework.common.helper.ListHelper;
import lombok.Setter;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "job_user", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_USER", columnNames = {"user_name"})})
public class User extends AbstractEntity implements SaltedAuthenticationInfo, AuthorizationInfo {

    private String userName;

    private String userPassword;

    private String passwordSalt;

    private List<Role> roleList;

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",joinColumns={@JoinColumn(name="user_id")}, inverseJoinColumns={@JoinColumn(name="role_id")})
    public List<Role> getRoleList() {
        return roleList;
    }

    @Transient
    @Override
    public Collection<String> getRoles() {
        List<String> roles = new ArrayList<>();
        if (!ListHelper.isEmpty(roles)) {
            roles.addAll(roleList.stream().map(Role::getRoleName).collect(Collectors.toList()));
        }
        return roles;
    }

    @Transient
    @Override
    public Collection<String> getStringPermissions() {
        List<String> stringPermissions = new ArrayList<>();
        if (!ListHelper.isEmpty(roleList)) {
            for (Role role : roleList) {
                List<Permission> permissions = role.getPermissionList();
                if (!ListHelper.isEmpty(permissions)) {
                    stringPermissions.addAll(permissions.stream().map(Permission::getPermissionName).collect(Collectors.toList()));
                }
            }
        }
        return stringPermissions;
    }

    @Transient
    @Override
    public Collection<org.apache.shiro.authz.Permission> getObjectPermissions() {
        return null;
    }

    @Transient
    @Override
    public ByteSource getCredentialsSalt() {
        return Hash.Util.bytes(passwordSalt);
    }

    @Transient
    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(userName, "DEFAULT");
    }

    @Transient
    @Override
    public Object getCredentials() {
        return userPassword;
    }

}
