package com.example.demo.Model.naming;

import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.stereotype.Component;
import org.hibernate.boot.model.naming.Identifier;


@Component
public class CustomPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {

    @Value("${env.prop.value}")
    private String environment;

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        String str = identifier.getText() + environment;
//        System.out.println("identifier is : " + str);
        return Identifier.toIdentifier(str);
    }
}