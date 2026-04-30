package br.eng.rodrigogml.mysteryrealms.application.user.service;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelo envio real de e-mails transacionais de usuário.
 *
 * @author ?
 * @since 30-04-2026
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String from;
    private final String publicBaseUrl;

    /**
     * Cria o serviço com o remetente configurado pelo Spring.
     *
     * @param mailSender remetente de e-mails
     * @param from endereço remetente da aplicação
     * @param publicBaseUrl URL pública usada para montar links enviados por e-mail
     */
    public EmailService(JavaMailSender mailSender,
            @Value("${mysteryrealms.mail.from}") String from,
            @Value("${mysteryrealms.publicBaseUrl}") String publicBaseUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.publicBaseUrl = publicBaseUrl;
    }

    /**
     * Envia o link de confirmação de cadastro.
     *
     * @param user usuário cadastrado
     * @param token token de confirmação
     */
    public void sendRegistrationConfirmation(UserEntity user, String token) {
        send(user.getEmail(), "Confirm your Mystery Realms account",
                "Welcome to Mystery Realms.\n\nConfirm your account using this link:\n"
                        + confirmationUrl(token) + "\n\nThis link expires in 48 hours.");
    }

    /**
     * Envia o link de confirmação de alteração de e-mail.
     *
     * @param email novo e-mail solicitado
     * @param token token de confirmação
     */
    public void sendEmailChangeConfirmation(String email, String token) {
        send(email, "Confirm your Mystery Realms email change",
                "Confirm your new email address using this link:\n"
                        + confirmationUrl(token) + "\n\nThis link expires in 24 hours.");
    }

    /**
     * Envia o link de confirmação de exclusão de conta.
     *
     * @param user usuário que solicitou exclusão
     * @param token token de confirmação
     */
    public void sendAccountDeletionConfirmation(UserEntity user, String token) {
        send(user.getEmail(), "Confirm your Mystery Realms account deletion",
                "Confirm permanent account deletion using this link:\n"
                        + confirmationUrl(token) + "\n\nThis link expires in 24 hours.");
    }

    private void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private String confirmationUrl(String token) {
        return publicBaseUrl.replaceAll("/+$", "") + "/confirm-email/" + token;
    }
}
