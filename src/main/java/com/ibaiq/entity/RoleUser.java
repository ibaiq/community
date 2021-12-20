package com.ibaiq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色实体类
 *
 * @author 十三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUser extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2179386086805076010L;

    private Integer userId;

    private Integer roleId;

}
