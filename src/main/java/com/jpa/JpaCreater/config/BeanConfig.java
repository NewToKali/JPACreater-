package com.jpa.JpaCreater.config;

import com.jpa.JpaCreater.pojo.JpaCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class BeanConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public String checkDatasource() throws SQLException, IOException {


        JpaCreator jpaCreator = new JpaCreator(dataSource);
        Connection conn = jpaCreator.getConntion();
        jpaCreator.writeJpaCoulmns(conn);
//        jpaCreator.printColumns(conn);

        return "test";
    }




}

