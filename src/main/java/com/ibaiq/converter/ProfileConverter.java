package com.ibaiq.converter;

import cn.hutool.core.util.ObjectUtil;
import com.ibaiq.entity.User;
import com.ibaiq.entity.vo.ProfileVo;
import org.mapstruct.InheritInverseConfiguration;
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
                      @Mapping(target = "permissions", expression = "java(getPermissions(po))"),
                      @Mapping(target = "isSysAdmin", expression = "java(getIsSysAdmin(po.isSysAdmin()))")
    })
    ProfileVo po2Vo(User po);

    /**
     * 反转
     */
    @InheritInverseConfiguration(name = "po2Vo")
    User vo2Po(ProfileVo vo);

    default Set<String> getPermissions(User user) {
        if (user.isAdmin()) {
            return user.getPermissions();
        }

        return null;
    }

    default Boolean getIsSysAdmin(Boolean isSysAdmin) {
        if (ObjectUtil.isNull(isSysAdmin) || !isSysAdmin) {
            return null;
        }
        return true;
    }

}
