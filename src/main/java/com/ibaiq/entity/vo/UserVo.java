package com.ibaiq.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibaiq.entity.Role;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 十三
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 843078519154777650L;

    private Integer id;

    private String nickname;

    private String username;

    private Date created;

    private String sex;

    private String avatar;

    private Boolean status;

    private Integer deleted;

    private Date updated;

    private List<Role> roles;

    private List<Integer> roleIds;

    private boolean admin;

}
