package com.ibaiq.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.ibaiq.entity.Message;
import com.ibaiq.utils.ip.IpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 十三
 */
@RestController
@Slf4j
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

    /**
     * 每日一句
     */
    @SneakyThrows
    @GetMapping("/sentence")
    public Message sentence() {
        // var body = HttpRequest.get("https://sentence.iciba.com/index.php?c=dailysentence&m=getdetail&title=" + LocalDate.now())
        var body = HttpRequest.get("https://api.xygeng.cn/one")
                          .execute().body();
        var note = JSONObject.parseObject(body).getJSONObject("data").get("content");
        return Message.success(note);
    }

}
