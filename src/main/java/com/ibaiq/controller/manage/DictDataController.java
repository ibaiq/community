package com.ibaiq.controller.manage;

import com.ibaiq.common.constants.Constants;
import com.ibaiq.controller.BaseController;
import com.ibaiq.entity.DictData;
import com.ibaiq.entity.Message;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 十三
 */
@RestController
@RequestMapping(Constants.MANAGE + "/dict/data")
public class DictDataController extends BaseController {

    @GetMapping("/list")
    @PreAuthorize("@permission.hasPermits('sys:dict:list')")
    public Message list(DictData dictData) {
        return Message.success(dictDataService.selectDictDataList(dictData));
    }

    @GetMapping("/type/{dictType}")
    public Message dictTypeDataInfo(@PathVariable String dictType) {
        return Message.success(dictDataService.selectDictDataByType(dictType));
    }

}
