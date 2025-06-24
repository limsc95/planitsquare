package com.planitsquare.holidaykeeper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
            if(converter instanceof MappingJackson2HttpMessageConverter jacksonConverter){
                List<MediaType> supportMediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
                supportMediaTypes.add(MediaType.valueOf("text/json"));
                jacksonConverter.setSupportedMediaTypes(supportMediaTypes);
            }
        }

        return restTemplate;
    }
}
