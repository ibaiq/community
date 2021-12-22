package com.ibaiq.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.entity.AccessLog;
import com.ibaiq.mapper.AccessLogMapper;
import com.ibaiq.service.AccessLogService;
import com.ibaiq.service.BaseService;
import com.ibaiq.utils.ip.AddressUtils;
import com.ibaiq.utils.ip.IpUtils;
import lombok.val;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 十三
 */
@Service
public class AccessLogServiceImpl extends BaseService<AccessLogMapper, AccessLog> implements AccessLogService {

    @Override
    @Async("taskExecutor")
    public void save(String accessText, String ip) {
        String data_json = Base64.decodeStr(accessText);
        JSONObject data = JSONObject.parseObject(data_json);
        var accessLog = new AccessLog();
        accessLog.setTime(new Date());
        accessLog.setIp(ip);
        accessLog.setUrl(data.getString("url"));
        accessLog.setLocation(AddressUtils.getCityInfo(ip));
        var user = data.getJSONObject("user");
        if (ObjectUtil.isNotEmpty(user)) {
            accessLog.setUsername(user.getString("username"));
        } else {
            try {
                var username = tokenUtils.getUsername(data.getString("token"));
                accessLog.setUsername(username);
            } catch (Exception e) {
                accessLog.setUsername(Constants.ANONYMOUS);
            }
        }
        redis.listSet("test", accessLog);
    }

}
