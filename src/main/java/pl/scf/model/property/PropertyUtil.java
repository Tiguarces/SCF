package pl.scf.model.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyUtil {

    @Bean
    @ConfigurationProperties(prefix = "activation-messages")
    public ActivateAccountProperty activateAccountProperty() {
        return new ActivateAccountProperty();
    }

    @Bean
    @ConfigurationProperties(prefix = "ver-token")
    public EmailProperty emailProperty() {
        return new EmailProperty();
    }

    @Bean
    @ConfigurationProperties(prefix = "user")
    public InitializerProperty initializerProperty() {
        return new InitializerProperty();
    }
}
