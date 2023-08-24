package com.green.babymeal;

import com.green.babymeal.common.config.properties.AppProperties;
import com.green.babymeal.common.config.properties.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class, CorsProperties.class })
public class BabymealApplication {

    public static void main(String[] args) {
        SpringApplication.run(BabymealApplication.class, args);
    }

}
