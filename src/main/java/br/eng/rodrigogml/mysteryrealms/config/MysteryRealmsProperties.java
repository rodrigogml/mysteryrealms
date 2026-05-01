package br.eng.rodrigogml.mysteryrealms.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades centralizadas da aplicação Mystery Realms.
 *
 * @author ?
 * @since 01-05-2026
 */
@Validated
@ConfigurationProperties(prefix = "mysteryrealms")
public class MysteryRealmsProperties {

    @Valid
    private MailProperties mail = new MailProperties();

    @NotBlank
    @URL
    private String publicBaseUrl;

    public MailProperties getMail() {
        return mail;
    }

    public void setMail(MailProperties mail) {
        this.mail = mail;
    }

    public String getPublicBaseUrl() {
        return publicBaseUrl;
    }

    public void setPublicBaseUrl(String publicBaseUrl) {
        this.publicBaseUrl = publicBaseUrl;
    }

    /**
     * Propriedades de e-mail da aplicação.
     *
     * @author ?
     * @since 01-05-2026
     */
    public static class MailProperties {

        @NotBlank
        private String from;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
