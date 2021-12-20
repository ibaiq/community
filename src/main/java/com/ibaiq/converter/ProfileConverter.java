package com.ibaiq.converter;

import com.ibaiq.entity.User;
import com.ibaiq.entity.vo.ProfileVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;

/**
 * @author 十三
 */
@Mapper(componentModel = "spring")
public interface ProfileConverter {

    @Mappings({
                      @Mapping(target = "nickname", source = "nickname"),
                      @Mapping(target = "username", source = "username"),
                      @Mapping(target = "sex", source = "sex"),
                      @Mapping(target = "avatar", source = "avatar"),
                      @Mapping(target = "created", source = "created"),
                      @Mapping(target = "roles", source = "roles"),
                      @Mapping(target = "permissions", expression = "java(getPermissions(user))"),
                      @Mapping(target = "admin", source = "admin"),
                      @Mapping(target = "isSysAdmin", expression = "java(getIsSysAdmin(user.isSysAdmin()))")
    })
    ProfileVo po2Vo(User user);

    default Set<String> getPermissions(User user) {
        if (user.isAdmin()) {
            return user.getPermissions();
        }

        return null;
    }

    default Boolean getIsSysAdmin(Boolean isSysAdmin) {
        if (!isSysAdmin) {
            return null;
        }
        return isSysAdmin;
    }

}
