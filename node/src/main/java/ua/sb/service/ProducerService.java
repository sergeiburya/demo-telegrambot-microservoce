package ua.sb.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * @author Serhii Buria
 */
public interface ProducerService {
    void producerAnswer(SendMessage sendMessage);
}
