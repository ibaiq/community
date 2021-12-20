package com.ibaiq.entity.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 十三
 */
@Data
public class UserUpdateRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7166817094413228338L;

    private Integer id;

    private String nickname;

    private String username;

    private String password;

    private String sex;

    private boolean status;

}
