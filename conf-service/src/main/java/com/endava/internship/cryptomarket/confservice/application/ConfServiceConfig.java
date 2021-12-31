package com.endava.internship.cryptomarket.confservice.application;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@Import({WebMvcConfiguration.class})
@EnableJpaRepositories(basePackages = "com.endava.internship.cryptomarket.confservice.data")
@ComponentScan(basePackages = {
        "com.endava.internship.cryptomarket.confservice.api",
        "com.endava.internship.cryptomarket.confservice.business",
        "com.endava.internship.cryptomarket.confservice.data"
})
@PropertySource("classpath:application.properties")
@EntityScan(basePackages = "com.endava.internship.cryptomarket.confservice.data.model")
public class ConfServiceConfig {

    @Bean
    public DataSource hikariDataSource(
            @Value("${datasource.url}") String url,
            @Value("${datasource.username}") String username,
            @Value("${datasource.password}") String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }
}