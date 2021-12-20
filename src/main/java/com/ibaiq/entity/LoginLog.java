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
public class LoginLog extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2165065264420590925L;

    private String username;
    private String ip;
    private String loginLocation;
    private String browser;
    private String os;
    private Boolean status;
    private String msg;
    private Date loginTime;

}
