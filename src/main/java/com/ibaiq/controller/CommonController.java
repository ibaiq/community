package com.ibaiq.controller;

import com.ibaiq.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 十三
 */
@RestController
@Slf4j
public class CommonController extends BaseController {

    /**
     * 记录前端访问日志
     */
    @PostMapping
    public void access() {
        String access = request.getHeader("Access");
        val ip = IpUtils.getIpAddress(request);
        accessLogService.save(access, ip);
    }

}
