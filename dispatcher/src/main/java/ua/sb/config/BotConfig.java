package ua.sb.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${telegram.botName}")
    String botUserName;

    @Value("${telegram.botToken}")
    String token;

    @Value("${telegram.botOwnerId}")
    private Long botOwnerId;
}
