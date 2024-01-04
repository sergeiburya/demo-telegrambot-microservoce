package ua.sb.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii Buria
 */
public interface UpdateProducer {
    void produce(String rabbitQueue, Update update);
}
