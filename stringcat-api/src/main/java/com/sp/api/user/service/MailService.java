package com.sp.api.user.service;

import com.sp.api.user.dto.MailReqDto;
import com.sp.domain.email.Email;
import com.sp.domain.email.EmailRepository;
import com.sp.exception.type.ErrorCode;
import com.sp.exception.type.StringcatCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {

    private final UserService userService;
    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String stringcat;

    private static final List<String> codeFactors = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    public Email findByIdAndExpiredAtAfterAndExpired(Long id) {
        Optional<Email> verifiedToken = emailRepository.findByIdAndExpiredAtAfterAndExpired(id, LocalDateTime.now(), false);

        return verifiedToken.orElseThrow(() -> new StringcatCustomException("정보를 찾을 수 없습니다.", ErrorCode.VALIDATION_EXCEPTION));
    }

    public static String issueCode() {
        StringBuilder issuedCode = new StringBuilder();
        Collections.shuffle(codeFactors);

        for(int i=0; i<6; i++) {
            issuedCode.append(codeFactors.get(i));
        }

        return issuedCode.toString();
    }

    public void sendMail(MailReqDto.MailTo form) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(form.getEmail());
        mailMessage.setSubject("[stringcat] Here is your password reset request!");
        mailMessage.setText(form.getContent());

        javaMailSender.send(mailMessage);
    }


}
