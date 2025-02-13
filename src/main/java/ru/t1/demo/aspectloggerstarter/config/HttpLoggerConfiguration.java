package ru.t1.demo.aspectloggerstarter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.t1.demo.aspectloggerstarter.aspect.HttpLoggingAspect;

@Configuration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@EnableAspectJAutoProxy
public class HttpLoggerConfiguration {

    @Bean
    @ConditionalOnProperty(name = "enabled", havingValue = "true", matchIfMissing = true)
    public HttpLoggingAspect httpLoggingAspect(HttpLoggingProperties properties) {
        return new HttpLoggingAspect(properties);
    }


}
