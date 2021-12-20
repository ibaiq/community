package com.ibaiq.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 上传文件服务接口
 *
 * @author 十三
 */
public interface UploadService {

    /**
     * 上传图片
     *
     * @param file    文件
     * @param request 请求域
     * @return 图片地址
     */
    String uploadImage(MultipartFile file, String childPath, HttpServletRequest request);

}
