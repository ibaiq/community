package com.ibaiq.controller.manage;

import com.ibaiq.common.annotation.Log;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.common.enums.BusinessType;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.Message;
import com.ibaiq.entity.SysConfig;
import com.ibaiq.utils.spring.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 系统配置控制器
 *
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE + "/config")
public class SysConfigController extends BaseController {

    @GetMapping("/{key}")
    @PreAuthorize("hasAuthority('sys:config:query')")
    public Message config(@PathVariable String key) {
        return Message.success(sysConfigService.find(key));
    }

    @GetMapping("/value/{key}")
    // @PreAuthorize("hasAuthority('sys:config:query')")
    public Message getConfigValue(@PathVariable String key) {
        return Message.success(sysConfigService.getKeyValue(key));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:config:list')")
    public Message list(@RequestParam(defaultValue = "1") String pageNum, @RequestParam(defaultValue = "10") String pageSize, SysConfig config) {
        return Message.success(sysConfigService.list(pageNum, pageSize, config));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('sys:config:modify')")
    @Log(module = "系统配置", businessType = BusinessType.UPDATE)
    public Message modify(@RequestBody SysConfig config) {
        config.setUpdateBy(SecurityUtils.getUsername());
        config.setUpdated(new Date());
        sysConfigService.modifyConfig(config);
        return Message.success();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('sys:config:save')")
    @Log(module = "系统配置", businessType = BusinessType.INSERT)
    public Message add(@RequestBody SysConfig config) {
        config.setCreateBy(SecurityUtils.getUsername());
        config.setCreated(new Date());
        sysConfigService.add(config);
        return Message.success();
    }

}
