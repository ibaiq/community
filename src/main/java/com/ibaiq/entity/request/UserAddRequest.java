package com.ibaiq.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author 十三
 */
@Data
public class UserAddRequest {

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Pattern(regexp = "[012]", message = "请选择性别「男，女，未知」")
    private String sex;

    @NotNull
    private Boolean status;

}
