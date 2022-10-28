// package org.rone.web.config;
//
// import com.alibaba.druid.pool.DruidDataSource;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//
// import javax.sql.DataSource;
//
// /**
//  * 多数据源的配置
//  * 该配置仅jdbcTemplate使用，无法给mybatis使用
//  * @author rone
//  */
// @Configuration
// public class MoreDataSourceConfig {
//
//     @Bean
//     @ConfigurationProperties(prefix = "spring.datasource")
//     DataSource dataSource() {
//         return new DruidDataSource();
//     }
//
//     @Bean
//     JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
//         return new JdbcTemplate(dataSource);
//     }
//
//     @Bean
//     NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
//         return new NamedParameterJdbcTemplate(dataSource);
//     }
//
//     @Bean
//     @ConfigurationProperties(prefix = "spring.datasource.second")
//     DataSource dataSourceTwo() {
//         return new DruidDataSource();
//     }
//
//     @Bean
//     JdbcTemplate secondJdbcTemplate(@Qualifier("dataSourceTwo") DataSource dataSourceTwo) {
//         return new JdbcTemplate(dataSourceTwo);
//     }
// }
