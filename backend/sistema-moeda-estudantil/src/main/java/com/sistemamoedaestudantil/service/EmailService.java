package com.sistemamoedaestudantil.service;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Singleton
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    private final boolean enabled;
    private final String from;
    private final String smtpHost;
    private final int smtpPort;
    private final String smtpUsername;
    private final String smtpPassword;
    private final boolean smtpAuth;
    private final boolean smtpStartTls;

    public EmailService(
            @Value("${email.enabled:false}") boolean enabled,
            @Value("${email.from}") String from,
            @Value("${email.smtp.host}") String smtpHost,
            @Value("${email.smtp.port}") int smtpPort,
            @Value("${email.smtp.username:}") String smtpUsername,
            @Value("${email.smtp.password:}") String smtpPassword,
            @Value("${email.smtp.auth:false}") boolean smtpAuth,
            @Value("${email.smtp.starttls:false}") boolean smtpStartTls) {
        this.enabled = enabled;
        this.from = from;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.smtpAuth = smtpAuth;
        this.smtpStartTls = smtpStartTls;
    }

    public void enviarMoedasParaAluno(String emailAluno, String nomeAluno, String nomeProfessor,
                                      int valor, String motivo) {
        String assunto = "Você recebeu moedas de mérito!";
        String corpo = "Olá, " + nomeAluno + "!\n\n"
                + "O professor " + nomeProfessor + " enviou " + valor + " moeda(s) para você.\n\n"
                + "Motivo: " + motivo + "\n\n"
                + "Acesse o sistema para consultar seu saldo e extrato.\n";
        enviar(emailAluno, assunto, corpo);
    }

    public void enviarConfirmacaoDistribuicaoProfessor(String emailProfessor, String nomeProfessor,
                                                       String nomeAluno, int valor, String motivo) {
        String assunto = "Confirmação de envio de moedas";
        String corpo = "Olá, " + nomeProfessor + "!\n\n"
                + "Confirmamos o envio de " + valor + " moeda(s) para o aluno " + nomeAluno + ".\n\n"
                + "Motivo registrado: " + motivo + "\n\n"
                + "Consulte seu extrato no sistema para mais detalhes.\n";
        enviar(emailProfessor, assunto, corpo);
    }

    public void enviarCupomAluno(String emailAluno, String nomeAluno, String nomeVantagem,
                                 String nomeEmpresa, int custo, String codigoCupom) {
        String assunto = "Seu cupom de resgate — " + nomeVantagem;
        String corpo = "Olá, " + nomeAluno + "!\n\n"
                + "Seu resgate foi confirmado.\n\n"
                + "Vantagem: " + nomeVantagem + "\n"
                + "Empresa: " + nomeEmpresa + "\n"
                + "Custo: " + custo + " moeda(s)\n"
                + "Código do cupom: " + codigoCupom + "\n\n"
                + "Apresente este código na troca presencial.\n";
        enviar(emailAluno, assunto, corpo);
    }

    public void enviarCupomEmpresa(String emailEmpresa, String razaoSocial, String nomeAluno,
                                   String nomeVantagem, int custo, String codigoCupom) {
        String assunto = "Novo resgate de vantagem — código " + codigoCupom;
        String corpo = "Olá, " + razaoSocial + "!\n\n"
                + "Um aluno resgatou uma vantagem da sua empresa.\n\n"
                + "Aluno: " + nomeAluno + "\n"
                + "Vantagem: " + nomeVantagem + "\n"
                + "Custo: " + custo + " moeda(s)\n"
                + "Código do cupom: " + codigoCupom + "\n\n"
                + "Utilize este código para conferir a troca presencial.\n";
        enviar(emailEmpresa, assunto, corpo);
    }

    private void enviar(String destinatario, String assunto, String corpo) {
        if (!enabled) {
            LOG.info("[EMAIL desabilitado] Para: {} | Assunto: {}\n{}", destinatario, assunto, corpo);
            return;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", String.valueOf(smtpPort));
            props.put("mail.smtp.auth", String.valueOf(smtpAuth));
            props.put("mail.smtp.starttls.enable", String.valueOf(smtpStartTls));

            Session session;
            if (smtpAuth) {
                session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(smtpUsername, smtpPassword);
                    }
                });
            } else {
                session = Session.getInstance(props);
            }

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(assunto, "UTF-8");
            message.setText(corpo, "UTF-8");

            Transport.send(message);
            LOG.info("E-mail enviado para {}", destinatario);
        } catch (Exception ex) {
            LOG.error("Falha ao enviar e-mail para {}: {}", destinatario, ex.getMessage());
        }
    }
}
