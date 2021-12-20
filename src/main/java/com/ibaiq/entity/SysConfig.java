package com.ibaiq.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 十三
 */
@TableName("config")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysConfig extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String configName;

    private String configKey;

    private String configValue;

    private Boolean configType;

    private String createBy;

    private Date created;

    private String updateBy;

    private Date updated;

    private String remark;

}
