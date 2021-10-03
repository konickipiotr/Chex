package com.chex.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;

//@Configuration
public class ExternalConfig {

    @Bean
    public DataSource getDataSource() {
        Map<String, String> getenv = System.getenv();
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(getDataSourceUrl())
                .username(System.getenv("DBUSERNAME"))
                .password(System.getenv("DBPASSWORD"))
                //.password("xxx")
                .build();
    }

    private String getDataSourceUrl() {
        return "jdbc:mysql://localhost:3306/chexdb?useUnicode=true&characterEncoding=utf8&useSSL=false&useJDBCComplaintTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
//                + System.getenv("DBUSERNAME") + "/"
//                + System.getenv("DBUSERNAME")
//                + "?useUnicode=true&characterEncoding=utf8&useSSL=false&useJDBCComplaintTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
//                + "?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false";
    }
}
