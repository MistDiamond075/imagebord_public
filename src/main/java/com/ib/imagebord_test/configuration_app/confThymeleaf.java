package com.ib.imagebord_test.configuration_app;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class confThymeleaf {
    @Value(value = "${path.threadfiles}")
    private String THREAD_FILES_DIR;

    @Value(value = "${path.cssfiles}")
    private String CSS_FILES_DIR;

    @Value(value = "${path.jsfiles}")
    private String JS_FILES_DIR;

    @Value(value = "${server.port}")
    private String SERVER_PORT;

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);

        // Добавляем глобальные переменные
        Map<String, Object> staticVariables = new HashMap<>();
        staticVariables.put("threadFiles", THREAD_FILES_DIR);
        staticVariables.put("cssfiles", CSS_FILES_DIR);
        staticVariables.put("jsfiles", JS_FILES_DIR);
        staticVariables.put("svport", SERVER_PORT);

        // Устанавливаем глобальные переменные
        viewResolver.setStaticVariables(staticVariables);

        return viewResolver;
    }
}
