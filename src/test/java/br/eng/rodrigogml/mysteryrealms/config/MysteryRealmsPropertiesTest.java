package br.eng.rodrigogml.mysteryrealms.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes das propriedades obrigatórias da aplicação.
 *
 * @author ?
 * @since 06-05-2026
 */
class MysteryRealmsPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(PropertiesTestConfig.class);

    @Test
    void contextFailsWhenRequiredEmailPropertiesAreMissing() {
        contextRunner.run(context -> {
            assertThat(context).hasFailed();
            assertThat(stackTrace(context.getStartupFailure()))
                    .contains("mysteryrealms.mail.from")
                    .contains("mysteryrealms.publicBaseUrl");
        });
    }

    @TestConfiguration
    @EnableConfigurationProperties(MysteryRealmsProperties.class)
    static class PropertiesTestConfig {

        @Bean
        LocalValidatorFactoryBean validator() {
            return new LocalValidatorFactoryBean();
        }
    }

    private static String stackTrace(Throwable throwable) {
        StringBuilder trace = new StringBuilder();
        while (throwable != null) {
            trace.append(throwable).append('\n');
            for (StackTraceElement element : throwable.getStackTrace()) {
                trace.append("\tat ").append(element).append('\n');
            }
            throwable = throwable.getCause();
        }
        return trace.toString();
    }
}
