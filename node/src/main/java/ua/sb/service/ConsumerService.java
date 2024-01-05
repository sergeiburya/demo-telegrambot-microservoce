package ua.sb.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii Buria
 */
public interface ConsumerService {
    void consumeTextMessageUpdate(Update update);

    void consumeDocMessageUpdate(Update update);

    void consumePhotoMessageUpdate(Update update);
}
