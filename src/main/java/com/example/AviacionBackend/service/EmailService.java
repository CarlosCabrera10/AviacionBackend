package com.example.AviacionBackend.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreo(String para, String asunto, String mensajeHtml) {

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(asunto);
            helper.setText(mensajeHtml, true); // HTML habilitado

            mailSender.send(mensaje);

            System.out.println("📨 Correo enviado a " + para);

        } catch (Exception e) {
            System.out.println("❌ Error enviando correo: " + e.getMessage());
        }
    }
}