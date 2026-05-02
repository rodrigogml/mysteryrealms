package br.eng.rodrigogml.mysteryrealms.application.user.service;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.UserEntity;
import br.eng.rodrigogml.mysteryrealms.config.MysteryRealmsProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes do serviço de envio de e-mails transacionais.
 *
 * @author ?
 * @since 30-04-2026
 */
@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Test
    void sendRegistrationConfirmation_enviaLinkComToken() {
        MysteryRealmsProperties properties = new MysteryRealmsProperties();
        MysteryRealmsProperties.MailProperties mail = new MysteryRealmsProperties.MailProperties();
        mail.setFrom("no-reply@mysteryrealms.local");
        properties.setMail(mail);
        properties.setPublicBaseUrl("http://localhost:8080/");
        EmailService service = new EmailService(mailSender, properties);
        UserEntity user = new UserEntity();
        user.setEmail("jogador@email.com");

        service.sendRegistrationConfirmation(user, "token-123");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        SimpleMailMessage message = captor.getValue();
        assertEquals("no-reply@mysteryrealms.local", message.getFrom());
        assertNotNull(message.getTo());
        assertEquals("jogador@email.com", message.getTo()[0]);
        assertEquals("Confirm your Mystery Realms account", message.getSubject());
        assertNotNull(message.getText());
        assertTrue(message.getText().contains("http://localhost:8080/confirm-email/token-123"));
    }
}
