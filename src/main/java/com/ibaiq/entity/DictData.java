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
public class DictData extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8700645455589712933L;

    private Integer dictSort;

    private String dictLabel;

    private String dictValue;

    private String dictType;

    private String cssClass;

    private String listClass;

    private Boolean isDefault;

    private Boolean status;

    private String createBy;

    private Date created;

    private String updateBy;

    private Date updated;

    private Boolean deleted;

    private String remark;

}
