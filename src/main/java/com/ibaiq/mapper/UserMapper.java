package com.ibaiq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ibaiq.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 操作（User）表接口
 *
 * @author 十三
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 批量删除
     *
     * @param ids id集合
     */
    void deleteBatchById(List<Integer> ids);

    /**
     * 批量恢复
     *
     * @param ids id集合
     */
    void recoverBatchById(List<Integer> ids);

}
