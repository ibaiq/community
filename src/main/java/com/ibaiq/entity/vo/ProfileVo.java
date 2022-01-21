package com.ibaiq.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibaiq.entity.Role;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author 十三
 */
@Data
public class ProfileVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -2009302429731125427L;

    private String nickname;

    private String username;

    private String sex;

    private String avatar;

    private Date created;

    private List<Role> roles;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<String> permissions;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean admin;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isSysAdmin;
}
