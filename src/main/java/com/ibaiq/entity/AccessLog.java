package com.ibaiq.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author 十三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccessLog extends BaseEntity {

    private String ip;

    private String url;

    private String username;

    private Date time;

    private String location;

}