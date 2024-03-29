package ua.sb.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii Buria
 */
public interface MainService {
    void processTextMessage(Update update);

    void processDocMessage(Update update);

    void processPhotoMessage(Update update);
}
