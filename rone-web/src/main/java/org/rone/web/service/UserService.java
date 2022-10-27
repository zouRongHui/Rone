package org.rone.web.service;

import org.rone.web.model.entity.User;

import java.util.List;

/**
 * 用户(User)表服务接口
 * @author rone
 */
public interface UserService {

    /**
     * 通过ID查询单条数据
     *
     * @param userNo 主键
     * @return 实例对象
     */
    User queryById(String userNo);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<User> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User insert(User user);

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param userNo 主键
     * @return 是否成功
     */
    boolean deleteById(String userNo);

    /**
     * 根据id集合获取姓名集合
     *
     * @param idList
     * @return
     */
    List<String> getNamesByIds(List<String> idList);
}
