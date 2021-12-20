package com.ibaiq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体类
 *
 * @author 十三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6888010658350868252L;

    private String name;

    private String title;

    private String description;

    private Boolean status;

    private Boolean type;

    private Date created;

    private Date updated;

}
