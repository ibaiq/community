package com.ibaiq.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ibaiq.utils.ip.IpUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 十三
 */
@RestController
public class CommonController extends BaseController {

    /**
     * 记录前端访问日志
     * 缓存到redis
     */
    @PostMapping("/access")
    public void access() {
        var access = request.getHeader("Access");
        if (ObjectUtil.isNotEmpty(access)) {
            var ip = IpUtils.getIpAddress(request);
            accessLogService.save(access, ip);
        }
    }

    /**
     * 访问首页，提示语
     */
    @RequestMapping
    public String index() {
        return "欢迎使用Ibaiq社区系统，请通过前端地址访问";
    }

}
