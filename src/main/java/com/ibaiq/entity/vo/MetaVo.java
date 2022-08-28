package com.ibaiq.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 十三
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MetaVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3479524525867354112L;

    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    /**
     * 路由属性（0目录，1菜单，2按钮）
     */
    private Integer type;

    // private String en_US;

    // private String zh_TW;

}
