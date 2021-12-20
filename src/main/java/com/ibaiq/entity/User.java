package com.ibaiq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibaiq.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户实体类
 *
 * @author 十三
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -375960793341730950L;

    private String nickname;

    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private Date created;

    private String sex;

    private String avatar;

    private Boolean status;

    private Integer deleted;

    private Date updated;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<Integer> roleIds;

    @TableField(exist = false)
    private Set<String> permissions;

    @TableField(exist = false)
    private boolean admin;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isAdmin() {
        if (StringUtils.isNotNull(roleIds) && !roleIds.isEmpty()) {
            return isAdmin(this.getId()) || roleIds.contains(1) || roleIds.contains(2);
        }
        return isAdmin(this.getId());
    }

    @JsonIgnore
    public boolean isSysAdmin() {
        return isAdmin(this.getId());
    }

    public static boolean isAdmin(Integer userId) {
        return StringUtils.isNotNull(userId) && 1 == userId;
    }

}
