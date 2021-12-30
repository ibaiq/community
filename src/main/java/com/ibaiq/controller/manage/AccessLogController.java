package com.ibaiq.controller.manage;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.AccessLog;
import com.ibaiq.entity.Message;
import com.ibaiq.utils.PageUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE + "/access")
public class AccessLogController extends BaseController {

    /**
     * 获取分页访问日志接口
     */
    @GetMapping("/list")
    public Message getAllAccessLogs(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        var list = redis.listGet(Constants.REDIS_KEY_ACCESS_LOG, 0, -1)
                          .stream().map(o -> (AccessLog) o)
                          .sorted((o1, o2) -> (int) (o2.getTime().getTime() - o1.getTime().getTime()))
                          .toList();
        return Message.success(PageUtils.startPage(list, pageNum, pageSize));
    }

    /**
     * 清空访问日志缓存接口
     */
    @DeleteMapping("/clear")
    public Message clear() {
        redis.del(Constants.REDIS_KEY_ACCESS_LOG);
        return Message.success();
    }

}
