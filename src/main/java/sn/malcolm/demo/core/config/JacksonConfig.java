package sn.malcolm.demo.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class JacksonConfig {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .findModulesViaServiceLoader(true)
                .modulesToInstall(new JavaTimeModule())
                .build();

        mapper.configOverride(Object.class)
                .setIgnorals(com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value
                        .forIgnoredProperties("hibernateLazyInitializer", "handler", "targetEntityClass", "target"));

        return new MappingJackson2HttpMessageConverter(mapper);
    }
}
