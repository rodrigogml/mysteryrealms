package br.eng.rodrigogml.mysteryrealms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configura componentes de criptografia para credenciais de usuários.
 *
 * @author ?
 * @since 06-05-2026
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Expõe um encoder BCrypt para senhas e códigos de recuperação de uso único.
     *
     * @return encoder BCrypt utilizado pela aplicação
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
