package com.ibaiq.controller.manage;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE)
public class CommonController extends BaseController {

    /**
     * 检查时候有进入后台的权限
     */
    @PostMapping
    public void check() {
    }

}
