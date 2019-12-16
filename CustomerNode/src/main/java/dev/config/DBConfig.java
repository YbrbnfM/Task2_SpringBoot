//package dev.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//
//@Configuration
//public class DBConfig {	
//    @Bean
//    public DataSource dataSource() {
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setDriverClassName("org.postgresql.Driver");
//        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/CustomerDB");
//        hikariConfig.setUsername("client");
//        hikariConfig.setPassword("1123");
//        //hikariConfig.setMaximumPoolSize(connectionSettings.getJdbcMaxPoolSize());
//        //hikariConfig.setPoolName("main");
//        return new HikariDataSource(hikariConfig);
//    }
//	
//}
