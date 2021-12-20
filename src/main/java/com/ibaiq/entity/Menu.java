package com.ibaiq.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ibaiq.common.constants.UserConstants;
import com.ibaiq.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单实体类
 *
 * @author 十三
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -348132841013954388L;

    private String title;

    private String icon;

    private String path;

    private String name;

    private String perms;

    private String component;

    private Integer sortNum;

    private Integer parentId;

    private Date created;

    private Date updated;

    private Integer type;

    private Integer deleted;

    private Boolean status;

    private Boolean visible;

    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();

    public boolean isSystem() {
        return isSystem(this.getId());
    }

    public static boolean isSystem(Integer menuId) {
        return StringUtils.isNotNull(menuId) && menuId.equals(UserConstants.MENU_ID);
    }

}
