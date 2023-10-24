package org.rone.web.dao;

import org.rone.core.jdk.exception.RoneException;
import org.rone.web.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JdbcTemplate使用示例
 * 单数据源时，在springboot启动配置中正确的配置了下面四个配置后即可在项目中直接使用 jdbcTemplate
 * spring:
 *   datasource:
 *     url: jdbc:mysql://localhost:3306/rone?useUnicode=true&characterEncoding=utf-8
 *     username: rone
 *     password: rone
 *     driver-class-name: com.mysql.cj.jdbc.Driver
 * 多数据源时 https://blog.51cto.com/u_9806927/5174734?b=totalstatistic
 *  1.配置数据源时已自定义的方式配置多个数据源配置(也是至少包含单数据源时的四个配置)
 *      见 spring.datasource.second 的配置
 *  2.自定义加载DataSource以及创建JdbcTemplate，因为使用的是自定义的配置，所以这里就需要
 *      {@link org.rone.web.config.MoreDataSourceConfig}
 *  3.使用，IOC注入时只能使用按名称注入
 *      org.rone.web.dao.JdbcTemplateDemoDao#secondJdbcTemplate
 * 常用的方法
 *  update：执行 新增、更新、删除操作，单条SQL；
 *  batchUpdate：批量执行 新增、更新、删除操作；
 *  call：执行存储过程、函数之类的；
 *  execute： 可执行任意正确的SQL；
 *  query：执行查询SQL；
 *  queryForMap：执行查询单条记录SQL，返回记录数不等于 1 时会直接报异常
 *  queryForObject：执行查询单条记录SQL，返回记录数不等于 1 时会直接报异常；
 *  queryForList：执行查询多条结果的SQL，始终返回一个list，可以为空list；
 *  queryForRowSet：执行查询SQL，用的比较少；
 *  queryForStream：执行查询SQL，返回一个流。
 * @author rone
 */
@Repository
public class JdbcTemplateDemoDao {

    private static Logger logger = LoggerFactory.getLogger(JdbcTemplateDemoDao.class);

    private final JdbcTemplate jdbcTemplate;
    /**
     * NamedParameterJdbcTemplate 可使用具名参数，便于代码维护，具体见下面的代码示例
     * 也可以这样来获取具名参数模板: namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
     */
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final PlatformTransactionManager platformTransactionManager;

    /**
     * 第二数据源，IOC注入时需要按照名称来注入
     */
    // private final JdbcTemplate secondJdbcTemplate;

    public JdbcTemplateDemoDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, PlatformTransactionManager platformTransactionManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.platformTransactionManager = platformTransactionManager;
    }

    public void demo() {
        String sql;
        Map<String, Object> paramMap;
        int executeNum;
        /** 写类型的语句 */
        // JdbcTemplate 批量执行写类型SQL语句
        sql = "INSERT INTO web_user(user_no, user_name, user_explain) VALUES(?,?,?)";
        List<Object[]> objList = new ArrayList<Object[]>(2);
        objList.add(new Object[] {"jdbc001", "Rose", "Rose@foxmail.com"});
        objList.add(new Object[] {"jdbc002", "Lucy", "Lucy@foxmail.com"});
        int[] executeNums = jdbcTemplate.batchUpdate(sql, objList);
        logger.info("JdbcTemplate 批量执行写类型SQL语句，执行结果: {}", executeNums);

        // JdbcTemplate 单个执行写类型SQL语句
        sql = "UPDATE web_user SET user_name=? WHERE user_no=?";
        executeNum = jdbcTemplate.update(sql, "Jack", "jdbc001");
        logger.info("JdbcTemplate 单个执行写类型SQL语句，执行结果: {}", executeNum);

        // NamedParameterJdbcTemplate 单个执行写类型SQL语句
        sql = "UPDATE web_user SET user_name=:userName WHERE user_no=:userNo";
        paramMap = new HashMap<>(2);
        paramMap.put("userName", "King");
        paramMap.put("userNo", "jdbc002");
        executeNum = namedParameterJdbcTemplate.update(sql, paramMap);
        logger.info("NamedParameterJdbcTemplate 单个执行写类型SQL语句，执行结果: {}", executeNum);

        // NamedParameterJdbcTemplate 当SQL中的参数名和POJO中的属性名一致时(下划线会自动使用驼峰命名格式转换)，可直接使用POJO作为参数执行SQL语句
        sql = "INSERT INTO web_user(user_no, user_name) VALUES(:userNo, :userName)";
        User newUser = new User("jdbc003", "Queen", "");
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(newUser);
        executeNum = namedParameterJdbcTemplate.update(sql, paramSource);
        // BeanPropertySqlParameterSource[] parameterSources = new BeanPropertySqlParameterSource[1];
        // parameterSources[0] = new BeanPropertySqlParameterSource(newUser);
        // executeNum = namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
        logger.info("NamedParameterJdbcTemplate 直接使用POJO作为参数执行写类型SQL语句，执行结果: {}", executeNum);


        /** 查类型的语句 */
        // JdbcTemplate 查询单条数据，返回原始结构的数据
        sql = "SELECT * FROM web_user WHERE user_no = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, "1");
        logger.info("JdbcTemplate 查询单条数据，返回原始结构的数据: {}", result);

        // JdbcTemplate 查询多条数据，返回原始结构的数据
        sql = "select * from web_user where 1=? and 1=?";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql, 1, 1);
        logger.info("JdbcTemplate 查询多条数据，返回原始结构的数据: {}", resultList);

        // JdbcTemplate 查询多条单属性数据
        sql = "select user_name from web_user where 1=?";
        List<String> userNameList = jdbcTemplate.queryForList(sql, String.class, "1");
        logger.info("JdbcTemplate 查询多条单属性数据: {}", userNameList);

        // JdbcTemplate 查询单个属性数据
        sql = "select user_name from web_user where user_no=?";
        String userName = jdbcTemplate.queryForObject(sql, String.class, "1");
        logger.info("JdbcTemplate 查询单个属性数据: {}", userName);

        // JdbcTemplate 查询统计，其余聚合函数同用法
        sql = "SELECT COUNT(*) FROM web_user";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        logger.info("JdbcTemplate 统计，当前 web_user 表中总数：{}", count);

        // JdbcTemplate 查询单条数据，返回Java对象。若查询条件无结果时会抛出异常 org.springframework.dao.EmptyResultDataAccessException
        sql = "SELECT * FROM web_user WHERE user_no = ?";
        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), "jdbc001");
        logger.info("JdbcTemplate 查询单条数据，返回Java对象: {}", user);

        // JdbcTemplate 查询多条数据，返回Java对象
        sql = "SELECT * FROM web_user";
        List<User> userList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        logger.info("JdbcTemplate 查询多条数据，返回Java对象: {}", userList);
        // userList = secondJdbcTemplate.query(sql, rowMapper);
        // logger.info("secondJdbcTemplate 多个结果的查询: {}", userList);

        // NamedParameterJdbcTemplate 查询单条数据，返回Java对象
        sql = "SELECT * FROM WEB_USER where user_no=:userNo";
        paramMap = new HashMap<>(1);
        paramMap.put("userNo", "1");
        User user1 = namedParameterJdbcTemplate.queryForObject(sql, paramMap, new BeanPropertyRowMapper<>(User.class));
        logger.info("NamedParameterJdbcTemplate 查询单条对象数据，{}", user1.toString());

        // NamedParameterJdbcTemplate 条件语句中使用 in 。在spring jdbcTemplate中使用in时，必须要使用NamedParameterJdbcTemplate具名的参数来查询
        sql = "select * from web_user where user_no in (:userNos)";
        List<String> ids = new ArrayList<>(3);
        ids.add("jdbc001");
        ids.add("jdbc002");
        ids.add("jdbc003");
        paramMap = new HashMap<>(1);
        paramMap.put("userNos", ids);
        List<User> userList1 = namedParameterJdbcTemplate.query(sql, paramMap, new BeanPropertyRowMapper<>(User.class));
        logger.info("NamedParameterJdbcTemplate 条件语句中使用 in : {}", userList1);
    }

    /**
     * 事务，手动回滚事务
     */
    public void transaction() {
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            String sql = "UPDATE web_user SET user_name=? WHERE user_no=?";
            int executeNum = jdbcTemplate.update(sql, "Jack1111", "1");
            logger.info("JdbcTemplate 单个执行写类型SQL语句，执行结果: {}", executeNum);
            if (executeNum > 0) {
                throw new RoneException("rone test");
            }

            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            platformTransactionManager.rollback(transactionStatus);
        }
    }
}
