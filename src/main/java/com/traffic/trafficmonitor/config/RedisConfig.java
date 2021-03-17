package com.traffic.trafficmonitor.config;

import com.traffic.trafficmonitor.commons.BytesToOffsetDateTimeConverter;
import com.traffic.trafficmonitor.commons.OffsetDateTimeToBytesConverter;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import java.util.Arrays;

@Configuration
@EnableRedisRepositories
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {


    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Add some specific configuration here. Key serializers, etc.
        return template;
    }

    @Bean
    public RedisCustomConversions redisCustomConversions(OffsetDateTimeToBytesConverter offsetToBytes,
                                                         BytesToOffsetDateTimeConverter bytesToOffset) {
        return new RedisCustomConversions(Arrays.asList(offsetToBytes, bytesToOffset));
    }

}


