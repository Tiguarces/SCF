package pl.scf.model.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    @Bean
    @ConfigurationProperties(prefix = "jwt-token")
    public JWTProperty jwtProperty() {
        return new JWTProperty();
    }

    @Bean
    @ConfigurationProperties(prefix = "admin-account")
    public AdministratorAccountProperty administratorAccountProperty() {
        return new AdministratorAccountProperty();
    }

    @Bean
    @ConfigurationProperties(prefix = "topic")
    public TopicCategoryProperty categoriesProperty() {
        return new TopicCategoryProperty();
    }
}
