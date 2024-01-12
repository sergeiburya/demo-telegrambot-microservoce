package ua.sb.service;

import ua.sb.dto.MailParams;

public interface ConsumerService {
    void consumeRegistrationMail(MailParams mailParams);

}