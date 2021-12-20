package com.ibaiq.converter;

import com.ibaiq.entity.User;
import com.ibaiq.entity.request.UserAddRequest;
import com.ibaiq.entity.request.UserUpdateRequest;
import com.ibaiq.entity.vo.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author 十三
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mappings({
                      @Mapping(target = "nickname", source = "nickname"),
                      @Mapping(target = "username", source = "username"),
                      @Mapping(target = "password", source = "password"),
                      @Mapping(target = "sex", source = "sex"),
                      @Mapping(target = "status", source = "status"),
                      @Mapping(target = "admin", ignore = true),
                      @Mapping(target = "avatar", ignore = true),
                      @Mapping(target = "beginTime", ignore = true),
                      @Mapping(target = "created", ignore = true),
                      @Mapping(target = "deleted", ignore = true),
                      @Mapping(target = "endTime", ignore = true),
                      @Mapping(target = "id", ignore = true),
                      @Mapping(target = "permissions", ignore = true),
                      @Mapping(target = "roleIds", ignore = true),
                      @Mapping(target = "roles", ignore = true),
                      @Mapping(target = "updated", ignore = true)
    })
    User request2Po(UserAddRequest userAddRequest);

    @Mappings({
                      @Mapping(target = "id", source = "id"),
                      @Mapping(target = "nickname", source = "nickname"),
                      @Mapping(target = "username", source = "username"),
                      @Mapping(target = "password", source = "password"),
                      @Mapping(target = "sex", source = "sex"),
                      @Mapping(target = "status", source = "status"),
                      @Mapping(target = "admin", ignore = true),
                      @Mapping(target = "avatar", ignore = true),
                      @Mapping(target = "beginTime", ignore = true),
                      @Mapping(target = "created", ignore = true),
                      @Mapping(target = "deleted", ignore = true),
                      @Mapping(target = "endTime", ignore = true),
                      @Mapping(target = "permissions", ignore = true),
                      @Mapping(target = "roleIds", ignore = true),
                      @Mapping(target = "roles", ignore = true),
                      @Mapping(target = "updated", ignore = true)
    })
    User request2Po(UserUpdateRequest userUpdateRequest);

    UserVo po2Vo(User user);

}
