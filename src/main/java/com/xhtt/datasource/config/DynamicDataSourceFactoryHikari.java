/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xhtt.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.xhtt.datasource.properties.DataSourceProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.SQLException;

/**
 * DruidDataSource
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public class DynamicDataSourceFactoryHikari {

    public static HikariDataSource buildDruidDataSource(DataSourceProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getUrl()); //数据源
        config.setUsername(properties.getUsername()); //用户名
        config.setPassword(properties.getPassword()); //密码
        config.setMaxLifetime(28700000L);
        config.addDataSourceProperty("cachePrepStmts", "true"); //是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("prepStmtCacheSize", "250"); //连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); //单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("useServerPrepStmts", "true"); //新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }
}
