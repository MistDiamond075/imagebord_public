package com.ib.imagebord_test.configuration_app;

import com.ib.imagebord_test.configuration_app.filter.CookieFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class confApp {
    @Bean
    public FilterRegistrationBean<Filter> cookieFilterRegistration(CookieFilter cookieFilter) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(cookieFilter);
        registrationBean.addUrlPatterns("/*"); 
        return registrationBean;
    }
}
