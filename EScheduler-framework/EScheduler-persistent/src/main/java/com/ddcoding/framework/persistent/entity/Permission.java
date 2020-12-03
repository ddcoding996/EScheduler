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
@Table(name = "job_permission", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_PERMISSION", columnNames = {"permission_name"})})
public class Permission extends AbstractEntity {

    private String permissionName;

    private String permissionDescription;

    private List<Role> roleList;

    @Column(name = "permission_name")
    public String getPermissionName() {
        return permissionName;
    }

    public String getPermissionDescription() {
        return permissionDescription;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissionList")
    public List<Role> getRoleList() {
        return roleList;
    }

}
