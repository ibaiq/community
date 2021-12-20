package com.ibaiq.entity.request;

import lombok.Data;

/**
 * @author 十三
 */
@Data
public class UserAddRequest {

    private String nickname;

    private String username;

    private String password;

    private String sex;

    private Boolean status;

}
