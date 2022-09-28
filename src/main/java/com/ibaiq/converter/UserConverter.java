package com.ibaiq.converter;

import com.ibaiq.entity.User;
import com.ibaiq.entity.request.UserAddRequest;
import com.ibaiq.entity.request.UserUpdateRequest;
import com.ibaiq.entity.vo.UserVo;
import org.mapstruct.Mapper;

/**
 * @author 十三
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    User request2Po(UserAddRequest request);

    User request2Po(UserUpdateRequest request);

    UserVo po2Vo(User po);

}
