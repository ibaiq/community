package com.ibaiq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 十三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictType extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -218256097549002860L;

    private String dictName;

    private String dictType;

    private Boolean status;

    private String createBy;

    private Date created;

    private String updateBy;

    private Date updated;

    private Boolean deleted;

    private String remark;

}
