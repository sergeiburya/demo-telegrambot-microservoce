package ua.sb.service;

import ua.sb.dto.MailParams;

public interface MailSenderService {

    void send(MailParams mailParams);
}
