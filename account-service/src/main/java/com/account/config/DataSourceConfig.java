//package com.account.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
//import javax.sql.DataSource;
//
//@Configuration
//@PropertySource("classpath:application.properties")
//public class DataSourceConfig {
//    @Value("${JDBC_DATABASE_DRIVER:}")
//    private String driverClass;
//    @Value("${JDBC_DATABASE_URL:}")
//    private String url;
//    @Value("${JDBC_DATABASE_USERNAME:}")
//    private String username;
//    @Value("${JDBC_DATABASE_PASSWORD:}")
//    private String password;
//
//
//
//    @Bean
//    public DataSource dataSource(){
//        System.out.println(driverClass+" "+ url+" "+username+" "+password);
//        DriverManagerDataSource source = new DriverManagerDataSource();
//        source.setDriverClassName(driverClass);
//        source.setUrl(url);
//        source.setUsername(username);
//        source.setPassword(password);
//        return source;
//    }
//
//    @Bean
//    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(){
//        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource());
//        return namedParameterJdbcTemplate;
//    }
//}
