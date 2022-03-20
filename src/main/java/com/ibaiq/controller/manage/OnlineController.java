package com.ibaiq.controller.manage;

import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.UserOnline;
import com.ibaiq.service.async.AsyncService;
import com.ibaiq.utils.PageUtils;
import com.ibaiq.utils.StringUtils;
import com.ibaiq.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author 十三
 */
@Slf4j
@RestController
@RequestMapping(Constants.MANAGE + "/online")
public class OnlineController extends BaseController {

    @GetMapping("/list")
    @PreAuthorize("@permission.hasPermits('sys:online:list')")
    public Message list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize,
                        String ip, String username) {
        Collection<String> keys = redis.keys(Constants.REDIS_PREFIX_TOKEN + "*");
        List<UserOnline> list = new ArrayList<>(Collections.emptyList());
        keys.forEach(key -> {
            UserOnline online = redis.get(key, UserOnline.class);
            if (StringUtils.isNotEmpty(ip) && StringUtils.isNotEmpty(username)) {
                if (ip.equals(online.getIp()) && username.equals(online.getUsername())) {
                    list.add(online);
                }
            } else if (StringUtils.isNotEmpty(ip)) {
                if (ip.equals(online.getIp())) {
                    list.add(online);
                }
            } else if (StringUtils.isNotEmpty(username)) {
                if (username.equals(online.getUsername())) {
                    list.add(online);
                }
            } else {
                list.add(online);
            }
        });

        list.sort((o1, o2) -> (int) (o2.getLoginTime().getTime() - o1.getLoginTime().getTime()));
        return Message.success(PageUtils.startPage(list, pageNum, pageSize));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("@permission.hasPermits('sys:online:force')")
    @Log(module = "在线用户", businessType = BusinessType.FORCE)
    public Message forceLogout(@PathVariable String uuid) {
        AsyncService async = SpringUtils.getBean(AsyncService.class);
        async.token_addBlacklist(uuid);
        return Message.success();
    }

}
