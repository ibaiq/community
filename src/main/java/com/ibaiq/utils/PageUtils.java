package com.ibaiq.utils;

import cn.hutool.core.map.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 *
 * @author 十三
 */
@SuppressWarnings("dep-ann")
public class PageUtils {

    /**
     * 开始分页
     *
     * @param pageNum  页码
     * @param pageSize 每页多少条数据
     */
    public static <T> Map<Object, Object> startPage(List<T> list, Integer pageNum, Integer pageSize) {
        if (list == null) {
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }

        Integer count = list.size(); // 记录总数
        int pageCount; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }

        int fromIndex; // 开始索引
        int toIndex; // 结束索引

        if (pageNum > pageCount) {
            return null;
        } else {
            fromIndex = (pageNum - 1) * pageSize;
            toIndex = pageNum * pageSize;
            toIndex = toIndex > count ? count : toIndex;
        }

        return MapUtil.builder().put("records", list.subList(fromIndex, toIndex)).put("total", count).put("size", pageSize).put("current", pageNum).put("pages", pageCount).map();
    }

}
