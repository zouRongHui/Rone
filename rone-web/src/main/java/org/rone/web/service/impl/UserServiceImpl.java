package org.rone.web.service.impl;

import org.rone.web.dao.UserDao;
import org.rone.web.model.entity.User;
import org.rone.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户(User)表服务实现类
 * @author rone
 */
@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    /**
     * 通过ID查询单条数据
     *
     * @param userNo 主键
     * @return 实例对象
     */
    @Override
    public User queryById(String userNo) {
        logger.debug(this.userDao.getUserByNo(userNo).toString());
        return this.userDao.queryById(userNo);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return this.userDao.queryAllByLimit(offset, limit, "user_no desc");
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getUserNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param userNo 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String userNo) {
        return this.userDao.deleteById(userNo) > 0;
    }

    /**
     * 根据id集合获取姓名集合
     *
     * @param idList
     * @return
     */
    @Override
    public List<String> getNamesByIds(List<String> idList) {
        if (idList == null || idList.isEmpty()) {
            return new ArrayList<>();
        }
        return this.userDao.getNamesByIds(idList);
    }
}
