package com.ibaiq.controller.manage;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.DictType;
import com.ibaiq.entity.Message;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE + "/dict/type")
public class DictTypeController extends BaseController {

    @GetMapping("/list")
    @PreAuthorize("@permission.hasPermits('sys:dict:list')")
    public Message list(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, DictType dictType) {
        return Message.success(dictTypeService.selectDictTypeList(pageNum, pageSize, dictType));
    }

}
