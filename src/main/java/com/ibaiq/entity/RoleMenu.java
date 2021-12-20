package com.ibaiq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单权限实体类
 *
 * @author 十三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleMenu extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2890567439085191163L;

    private Integer roleId;

    private Integer menuId;

}
