package com.green.babymeal.email;

import com.green.babymeal.auth.model.SignPwDto;
import com.green.babymeal.email.model.MailSendDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

@Component
@Slf4j
@Service
@EnableScheduling
public class EmailService {


    private final String user = "green502teamA@gmail.com";
    private final String password = "kqvrkfnjbemoclmb";
    private final String admin = "dlwlsrb0307@naver.com";

    @Autowired
    private final EmailMapper mapper;

    @Autowired
    private final PasswordEncoder PW_ENCODER;


    public EmailService(EmailMapper mapper, PasswordEncoder pwEncoder) {
        this.mapper = mapper;
        PW_ENCODER = pwEncoder;
    }

    // 앱2차비밀번호 !!! 비밀번호 넣으면 동작하지 않습니다
    // 팀 공동사용중, 비밀번호나 세팅 확인 필요시 슬랙참고해주세요



    public String send(MailSendDto dto) {

        log.info("메일 전송 시작");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(user, "babyfoodTest")); // 발신메일, 발신자이름
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(dto.getMailAddress())); // 수신자 메일 주소
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(admin, "안녕하세요"));
            message.setSubject(dto.getTitle()); //메일 제목을 입력
            message.setText(dto.getCtnt());
            Transport.send(message);
            log.info("메일 전송 완료");
            return "메일 전송 완료";

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "메일 전송 실패";
    }



    public String findPassword(String mail, String mobileNb) {
        // email을 기준으로 DB의 유저 정보와 비교
        SignPwDto dataDto = mapper.findPassword(mail);
        log.info(" : {}", dataDto.getIuser());

        if (mobileNb.equals(dataDto.getMobileNb())){
            log.info("회원정보 일치, 비밀번호 변경 시작");
            String pw = updPassword(); // 임시 비밀번호 생성
            log.info(pw);
            String encryptedPw = PW_ENCODER.encode(pw); // 비밀번호 암호화

            // DB의 비밀번호 변경
            mapper.updPassword(dataDto.getIuser(), encryptedPw);

            // 메일 발송
            MailSendDto dto = new MailSendDto();
            dto.setTitle("비밀번호 변경 메일입니다");
            dto.setCtnt("임시 비밀번호 : " + pw + "\n 임시 비밀번호를 이용하여 로그인 후, 사용하고자 하는 비밀번호로 변경하세요.");
            dto.setMailAddress(mail);
            send(dto);

            return "회원정보 일치 / 임시 비밀번호 메일 발송";
        }
        else {
            return "회원정보 불일치 / 확인 후 다시 시도하세요";
        }
    }

    public String updPassword() {
        // 임시 비밀번호 생성 -> 0~9, 알파벳 대소문자
        int index = 0;
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };

        StringBuffer password = new StringBuffer();
        Random random = new Random();

        final int PASSWORDLENGTH = 10; // 임시비밀번호 길이

        for (int i = 0; i < PASSWORDLENGTH ; i++) {
            double rd = random.nextDouble();
            index = (int) (charSet.length * rd);
            password.append(charSet[index]);
        }
        return password.toString();
    }

}
