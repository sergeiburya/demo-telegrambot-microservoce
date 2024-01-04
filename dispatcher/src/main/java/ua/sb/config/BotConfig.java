package ua.sb.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Serhii Buria
 */
@Configuration
@Data
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${telegram.botName}")
    private String botUserName;

    @Value("${telegram.botToken}")
    private String token;

    @Value("${telegram.botOwnerId}")
    private Long botOwnerId;
}
