package com.sp.api.auth.service;

import com.sp.api.auth.dto.MailReqDto;
import com.sp.domain.code.EmailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String stringcat;

    public static final String emailAuthKey = issueTmpPw();
    public static final String tmpPW = issueTmpPw();

    public static String issueTmpPw() {
        StringBuilder authKey = new StringBuilder();
        Random random = new Random();

        for(int i=0; i<8; i++) {
            int index = random.nextInt(2);

            switch (index) {
                case 0:
                    authKey.append((char) ((int) (random.nextInt(26)) + 97));
                    break;
                case 1:
                    authKey.append((char) ((int) (random.nextInt(26)) + 65));
                    break;
                case 2:
                    authKey.append(random.nextInt(10));
                    break;
            }
        }

        return authKey.toString();
    }

    public String sendEmail(String to, EmailType type) throws Exception {
        if (type == EmailType.VERIFIER) {
            MimeMessage authEmail = sendMailAuth(to);
            javaMailSender.send(authEmail);
            //            case PASSWORD_SENDER:
//                MimeMessage passwordEmail = sendTmpPW(to);
//                javaMailSender.send(passwordEmail);
//                break;
        }

        if (type == EmailType.VERIFIER) {
            return emailAuthKey;
        } else
            return tmpPW;
    }

    public String checkEmailCode(MailReqDto.MailTo request) throws Exception {
        String sentCode = sendEmail(request.getEmail(), request.getType());

        if(sentCode.equals(request.getCode())) {
            return request.getEmail();
        } else
            return "인증코드가 일치하지 않습니다.";
    }

    private MimeMessage sendMailAuth(String to) throws Exception {
        log.info("보내는 대상 :: {} ", to);
        log.info("인증번호 :: {}", emailAuthKey);

        MimeMessage authEmail = javaMailSender.createMimeMessage();
        authEmail.addRecipients(Message.RecipientType.TO, to); //수신인
        authEmail.setSubject("[Stringcat] 이메일 인증번호 안내"); //메일 제목

        String content = "";
        content+= "<div style='margin:100px;'>";
        content+= "<h1> 안녕하세요 Stringcat입니다. </h1>";
        content+= "<br>";
        content+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        content+= "<br>";
        content+= "<p>감사합니다!<p>";
        content+= "<br>";
        content+= "<div font-family:verdana';>";
        content+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        content+= "<div style='font-size:130%'>";
        content+= "CODE : <strong>";
        content+= emailAuthKey+"</strong><div><br/> ";
        content+= "</div>";
        authEmail.setText(content, "utf-8", "html");//내용
        authEmail.setFrom(new InternetAddress(stringcat,"Stringcat"));//보내는 사람

        return authEmail;
    }
}
