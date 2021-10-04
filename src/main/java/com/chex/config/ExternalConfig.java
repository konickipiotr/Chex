package com.chex.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.sql.DataSource;

@Configuration
public class ExternalConfig {

    @Bean
    @Profile("dev")
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url(getDataSourceUrl())
                .username(System.getenv("DBUSERNAME"))
                .password(System.getenv("DBPASSWORD"))
                .build();
    }

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(System.getenv("EMAILSENDER_EMAIL"));
        javaMailSender.setPassword(System.getenv("EMAILSENDER_PASSWORD"));
        return javaMailSender;
    }

    private String getDataSourceUrl() {
        return "jdbc:mysql://localhost:3306/chexdb?useUnicode=true&characterEncoding=utf8&useSSL=false&useJDBCComplaintTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    }
}
