package org.rone.web.dao;

import org.rone.web.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JdbcTemplate使用示例
 * @author rone
 */
@Repository
public class JdbcTemplateDemoDao {

    private static Logger logger = LoggerFactory.getLogger(JdbcTemplateDemoDao.class);

    /**
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
     */
    private final JdbcTemplate jdbcTemplate;
    /**
     * NamedParameterJdbcTemplate 可使用具名参数，便于代码维护
     * 也可以这样来获取具名参数模板: namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
     */
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * 第二数据源，IOC注入时需要按照名称来注入
     */
    // private final JdbcTemplate secondJdbcTemplate;

    public JdbcTemplateDemoDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void demo() {
        // JdbcTemplate 批量操作SQL语句
        String sql = "INSERT INTO user(user_no, user_name, user_explain) VALUES(?,?,?)";
        List<Object[]> objList = new ArrayList<Object[]>(2);
        objList.add(new Object[] {"jdbc001", "Rose", "Rose@foxmail.com"});
        objList.add(new Object[] {"jdbc002", "Lucy", "Lucy@foxmail.com"});
        jdbcTemplate.batchUpdate(sql, objList);

        // JdbcTemplate 查询单个结果，若查询条件无结果时会抛出异常 org.springframework.dao.EmptyResultDataAccessException
		sql = "SELECT * FROM user WHERE user_no = ?";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user = jdbcTemplate.queryForObject(sql, rowMapper, "jdbc001");
        logger.info("JdbcTemplate 查询单个结果: {}", user);

        // JdbcTemplate 多个结果的查询
		sql = "SELECT * FROM user";
		List<User> userList = jdbcTemplate.query(sql, rowMapper);
        logger.info("JdbcTemplate 多个结果的查询: {}", userList);
		// userList = secondJdbcTemplate.query(sql, rowMapper);
        // logger.info("secondJdbcTemplate 多个结果的查询: {}", userList);

        // JdbcTemplate 单个SQL语句的增删改
		sql = "UPDATE user SET user_name = ? WHERE user_no = ?";
		jdbcTemplate.update(sql, "Jack", "jdbc001");

        // JdbcTemplate 查询单个属性或做统计
		sql = "SELECT COUNT(1) FROM user";
		Long count = jdbcTemplate.queryForObject(sql, Long.class);
        logger.info("JdbcTemplate 查询单个属性或做统计。目前数据表中总数：{}", count);

        // NamedParameterJdbcTemplate 使用具名参数
		sql = "UPDATE user SET user_name = :userName WHERE user_no = :userNo";
		Map<String, Object> paramMap = new HashMap<>(2);
		paramMap.put("userName", "King");
		paramMap.put("userNo", "jdbc002");
		namedParameterJdbcTemplate.update(sql, paramMap);

        // NamedParameterJdbcTemplate 当SQL中的参数名和POJO中的属性名一致时，可直接使用POJO进行SQL操作
		sql = "INSERT INTO user(user_no, user_name) VALUES(:userNo, :userName)";
		User newUser = new User("jdbc003", "Queen", "fuck");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(newUser);
		namedParameterJdbcTemplate.update(sql, paramSource);

        // 在spring jdbcTemplate中使用in时，需要使用NamedParameterJdbcTemplate具名的参数来查询
        sql = "select * from user where user_no in (:userNos)";
        List<String> ids = new ArrayList<>(3);
        ids.add("jdbc001");
        ids.add("jdbc002");
        ids.add("jdbc003");
        paramMap = new HashMap<>(1);
        paramMap.put("userNos", ids);
        userList = namedParameterJdbcTemplate.query(sql, paramMap, new BeanPropertyRowMapper<>(User.class));
        logger.info("NamedParameterJdbcTemplate 使用 in 查询: {}", userList);
    }
}
