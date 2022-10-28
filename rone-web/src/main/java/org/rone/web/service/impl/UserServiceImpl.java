package org.rone.web.service.impl;

import org.rone.web.dao.UserDao;
import org.rone.web.model.entity.User;
import org.rone.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户(User)表服务实现类
 * 使用@Transactional 表明该类下的所有方法开启事务，也可以标注到具体的方法上表示该方法开启事务。
 * 使用xml文件来配置事务方式移步https://github.com/zouRongHui/program_old/blob/master/spring/src/main/resources/application-transaction.xml
 *  1.propagation 事务的传播，即在事务方法调用其他事务方法时，事务的工作模式。
 *  	常用的两个事务传播行为：
 *  		REQUIRED：默认项，共用同一个事务，即无论哪个子事务回滚，所有事务都回滚；
 *   	REQUIRES_NEW：开启新的事务，若子事务回滚只回滚当前事务，该属性需要配置在子方法中，
 *   					若子方法时通过throw Exception来触发事务回滚的话，该exception需要手动捕获，否则程序会在异常处终止执行；
 *      eg: propagation=Propagation.REQUIRED
 *  2.isolation 事务的隔离级别，事务并发中的读取脏数据、不可重复读取、幻读；
 *      eg: isolation=Isolation.READ_COMMITTED
 *  3.noRollbackFor、noRollbackForClassName、rollbackFor、rollbackForClassName
 *  	来指定哪些异常不需要回滚和只回滚哪些异常；
 *      eg: rollbackFor={Exception.class}
 *  4.readOnly(true/false) 指定事务是否为只读，便于帮助数据库引擎优化事务；
 *      eg: readOnly=true
 *  5.timeout(单位秒) 用来表明事务的最长执行时间，超时就会强制回滚；
 *      eg: timeout=3
 * 手动回滚事务 org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
 * @author rone
 */
@Service
@Transactional(rollbackFor = Exception.class)
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
