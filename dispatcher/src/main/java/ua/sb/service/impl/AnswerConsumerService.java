package ua.sb.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.sb.service.AnswerConsumer;

/**
 * @author Serhii Buria
 */
@Service
@Log4j
public class AnswerConsumerService implements AnswerConsumer {
    @Override
    public void consume(SendMessage sendMessage) {

    }
}
