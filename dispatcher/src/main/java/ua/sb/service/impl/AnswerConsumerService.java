package ua.sb.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ua.sb.service.AnswerConsumer;
import ua.sb.service.UpdateProcessor;

/**
 * @author Serhii Buria
 */
@Service
@Log4j
public class AnswerConsumerService implements AnswerConsumer {
    private final UpdateProcessor updateProcessor;

    public AnswerConsumerService(UpdateProcessor updateProcessor) {
        this.updateProcessor = updateProcessor;
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.answer-message}")
    public void consume(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}
