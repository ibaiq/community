package com.ibaiq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 操作日志实体类
 *
 * @author 十三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OperLog extends BaseEntity {

    private String module;

    private Integer businessType;

    private String method;

    private String requestMethod;

    private String operName;

    private String operUrl;

    private String operIp;

    private String operLocation;

    private String operParam;

    private String operResult;

    private Boolean status;

    private String errorMsg;

    private Date operTime;

    private Long consume;

}
