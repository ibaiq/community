package com.ibaiq.controller.manage;

import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.OperLog;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志控制器
 *
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE + "/operLog")
public class OperLogController extends BaseController {

    /**
     * 获取分页日志
     */
    @GetMapping("/list")
    @PreAuthorize("@permission.hasPermits('sys:log:operate')")
    public Message list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, OperLog operations) {
        return Message.success(operLogService.list(pageNum, pageSize, operations));
    }

    /**
     * 删除日志
     */
    @DeleteMapping
    @PreAuthorize("@permission.hasPermits('sys:log:operate:delete')")
    @Log(module = "日志管理", businessType = BusinessType.DELETE)
    public Message delete(@RequestBody List<Integer> ids) {
        operLogService.deleteById(ids);
        return Message.success();
    }

    /**
     * 清空日志
     */
    @DeleteMapping("/clear")
    @PreAuthorize("@permission.hasPermits('sys:log:operate:clear')")
    public Message clear() {
        operLogService.clear();
        return Message.success();
    }

}
