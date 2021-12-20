package com.ibaiq.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.ibaiq.common.enums.MessageEnum;
import com.ibaiq.config.IbaiqConfig;
import com.ibaiq.exception.BaseException;
import com.ibaiq.service.UploadService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 上传文件服务实现类
 *
 * @author 十三
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private IbaiqConfig ibaiq;

    @SneakyThrows
    @Override
    public String uploadImage(MultipartFile file, String childPath, HttpServletRequest request) {

        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            throw new BaseException(MessageEnum.UPLOAD_FILE_NULL);
        }

        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        List<String> extList = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");
        if (!extList.contains(suffixName)) {
            throw new BaseException(MessageEnum.ILLEGAL_FORMAT);
        }

        fileName = UUID.randomUUID().toString().replace("-", "") + suffixName;
        LocalDate date = LocalDate.now();
        String nowDay = date.getYear() + "/" + date.getMonthValue() + "/" + date.getDayOfMonth() + "/";
        String path = request.getContextPath() + "/profile/" + childPath + nowDay + (fileName);

        File dest = new File(ibaiq.getProfile() + childPath + nowDay + fileName);
        if (!dest.getParentFile().exists()) {
            if (!dest.getParentFile().mkdirs()) {
                throw new BaseException(MessageEnum.ERROR);
            }
        }

        file.transferTo(dest);

        return path;
    }

}
