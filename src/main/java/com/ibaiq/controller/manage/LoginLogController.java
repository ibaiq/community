package com.ibaiq.controller.manage;

import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.LoginLog;
import com.ibaiq.entity.Message;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志控制器
 *
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE + "/loginLog")
public class LoginLogController extends BaseController {

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:log:login')")
    public Message list(@RequestParam(defaultValue = "1") String pageNum, @RequestParam(defaultValue = "10") String pageSize, LoginLog loginLog) {
        return Message.success(logService.getAll(pageNum, pageSize, loginLog));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('sys:log:delete')")
    @Log(module = "登录日志", businessType = BusinessType.DELETE)
    public Message remove(@RequestBody List<Integer> ids) {
        logService.delete(ids);
        return Message.success();
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasAuthority('sys:log:clear')")
    @Log(module = "角色管理", businessType = BusinessType.CLEAN)
    public Message clear() {
        logService.clear();
        return Message.success();
    }

}
