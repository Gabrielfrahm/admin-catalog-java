package com.admin.catalog.infra;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.infra.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infra.category.persistence.CategoryRepository;
import com.admin.catalog.infra.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class,args);
    }
}