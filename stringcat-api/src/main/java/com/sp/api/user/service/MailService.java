package com.sp.api.user.service;

import com.sp.api.user.dto.MailReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(MailReqDto.MailTo form) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(form.getEmail());
        mailMessage.setSubject(form.getTitle());
        mailMessage.setText(form.getContent());

        javaMailSender.send(mailMessage);
    }

}
