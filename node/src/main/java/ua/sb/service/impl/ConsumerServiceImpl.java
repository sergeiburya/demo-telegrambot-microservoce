package ua.sb.service.impl;

import static ua.sb.model.RabbitQueue.DOC_MESSAGE_UPDATE;
import static ua.sb.model.RabbitQueue.PHOTO_MESSAGE_UPDATE;
import static ua.sb.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.sb.repositories.MainService;
import ua.sb.service.ConsumerService;

/**
 * @author Serhii Buria
 */
@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;

    public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumeTextMessageUpdate(Update update) {
        log.debug("Node: Text message is received");
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void consumeDocMessageUpdate(Update update) {
        log.debug("Node: Text message is received");
    }

    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void consumePhotoMessageUpdate(Update update) {
        log.debug("Node: Text message is received");
    }
}
