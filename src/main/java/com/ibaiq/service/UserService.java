package com.ibaiq.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ibaiq.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * （User）表服务接口
 *
 * @author 十三
 */
public interface UserService {

    /**
     * 查询一条记录
     *
     * @param id 管理员id
     * @return 查询到的对象
     */
    User findById(Integer id);

    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    void add(User user);

    /**
     * 分页查询所有用户 可根据条件查询
     *
     * @param pageNum  页码
     * @param pageSize 一页多少条数据
     * @param isDelete 是否查询已删除的
     * @return 分页数据
     */
    Page<User> getAll(String pageNum, String pageSize, User user, boolean isDelete);

    /**
     * 修改用户头像
     *
     * @param id      用户id
     * @param url     头像地址
     * @param request 请求
     */
    void modifyAvatar(Integer id, String url, HttpServletRequest request);

    /**
     * 修改用户信息
     *
     * @param user 信息
     */
    void modify(User user);

    /**
     * 获取已登录的信息
     *
     * @return 用户信息
     */
    User getInfo();

    /**
     * 获取用户的角色权限信息
     *
     * @param user 用户
     * @return 权限字符串
     */
    String getUserAuthorityInfo(User user);

    /**
     * 批量删除用户
     *
     * @param userIds 用户id
     * @param isDel   true删除false恢复
     */
    void delOrRestById(List<Integer> userIds, boolean isDel);

    void updateCache(Integer userId);

    void batchUpdateCache(List<Integer> userIds);

    /**
     * 重置密码
     */
    void resetPwd(User user);

    /**
     * 获取角色授权的用户
     */
    Page<User> getAuthorizeList(Integer pageNum, Integer pageSize, Integer roleId, User user);

    /**
     * 获取未授权的用户
     */
    Page<User> getUnAuthorizeList(Integer pageNum, Integer pageSize, Integer roleId, User user);

    /**
     * 用户修改信息
     */
    void userModify(User user, HttpServletRequest request);

    /**
     * 修改密码
     */
    void changePwd(String oldPwd, String newPwd, HttpServletRequest request);

    /**
     * 获取用户数量
     */
    Long getUserCount();

}
