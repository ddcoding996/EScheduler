package com.ddcoding.framework.persistent.entity;

import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

/**
 */
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "job_role", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_ROLE", columnNames = {"role_name"})})
public class Role extends AbstractEntity {

    private String roleName;

    private String roleDescription;

    private List<User> userList;

    private List<Permission> permissionList;

    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roleList")
    public List<User> getUserList() {
        return userList;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="role_permission",joinColumns={@JoinColumn(name="role_id")}, inverseJoinColumns={@JoinColumn(name="permission_id")})
    public List<Permission> getPermissionList() {
        return permissionList;
    }

}
