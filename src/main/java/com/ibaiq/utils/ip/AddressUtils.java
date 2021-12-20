package com.ibaiq.utils.ip;

import com.alibaba.fastjson.JSONObject;
import com.ibaiq.common.constants.Constants;
import com.ibaiq.entity.City;
import com.ibaiq.entity.SysConfig;
import com.ibaiq.utils.RedisUtils;
import com.ibaiq.utils.spring.SpringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author 十三
 */
public class AddressUtils {

    public static String getCityInfo(String ip) {
        String s = sendGet(ip, Constants.TENCENT_KEY);
        City result = JSONObject.parseObject(s, City.class);

        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (result.getStatus() == 0) {
            City.Result info = result.getResult();
            City.Result.AdInfo adInfo = info.getAdInfo();
            String nation = adInfo.getNation();
            String province = adInfo.getProvince();
            String city = adInfo.getCity();
            String district = adInfo.getDistrict();

            // return nation + "-" + province + "-" + city + "-" + district;
            RedisUtils redis = SpringUtils.getBean(RedisUtils.class);
            if (redis.hasKey(Constants.REDIS_PREFIX_CONFIG + "location") && Boolean.parseBoolean(redis.get(Constants.REDIS_PREFIX_CONFIG + "location", SysConfig.class).getConfigValue())) {
                return String.format("%s %s %s %s", nation, province, city, district).trim();
            } else {
                return String.format("%s %s %s", province, city, district).trim();
            }
        }
        return "";
    }

    //根据在腾讯位置服务上申请的key进行请求操作
    public static String sendGet(String ip, String key) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = "https://apis.map.qq.com/ws/location/v1/ip?ip=" + ip + "&key=" + key;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                              "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (Map.Entry entry : map.entrySet()) {
//                System.out.println(key + "--->" + entry);
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                              connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.toString();
    }

}

