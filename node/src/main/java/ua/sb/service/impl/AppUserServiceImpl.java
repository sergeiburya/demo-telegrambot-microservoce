package ua.sb.service.impl;

import static ua.sb.model.UserState.BASIC_STATE;
import static ua.sb.model.UserState.WAIT_FOR_EMAIL_STATE;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.sb.dto.MailParams;
import ua.sb.model.AppUser;
import ua.sb.repositories.AppUserRepositories;
import ua.sb.service.AppUserService;
import ua.sb.utils.CryptoTool;

@Log4j
@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepositories appUserRepositories;

    private final CryptoTool cryptoTool;

    @Value("${spring.rabbitmq.queues.registration-mail}")
    private String registrationMailQueue;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "You are already registered!";
        } else if (appUser.getEmail() != null) {
            return "An email has already been sent to you "
                    + "Follow the link in the email to confirm your registration.";
        }
        appUser.setState(WAIT_FOR_EMAIL_STATE);
        appUserRepositories.save(appUser);
        return "Please enter your email:";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            var emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            return "Please enter a correct email. To cancel the command, enter /cancel";
        }

        var appUserOpt = appUserRepositories.findByEmail(email);
        if (appUserOpt.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(BASIC_STATE);
            appUser = appUserRepositories.save(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            sendRegistrationMail(cryptoUserId, email);

            return "A letter has been sent to you by email."
                    + "Follow the link in the email to confirm your registration.";
        } else {
            return "This email is already in use. Please enter a valid email."
                    + " To cancel the command, enter /cancel";
        }
    }

    private void sendRegistrationMail(String cryptoUserId, String email) {
        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        rabbitTemplate.convertAndSend(registrationMailQueue, mailParams);
    }
}
