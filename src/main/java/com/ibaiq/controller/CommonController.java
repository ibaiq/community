package com.ibaiq.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ibaiq.utils.ip.IpUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 十三
 */
@RestController
public class CommonController extends BaseController {

    /**
     * 记录前端访问日志
     */
    @PostMapping
    public void access() {
        var access = request.getHeader("Access");
        if (ObjectUtil.isNotEmpty(access)) {
            var ip = IpUtils.getIpAddress(request);
            accessLogService.save(access, ip);
        }
    }

}
