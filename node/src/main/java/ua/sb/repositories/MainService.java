package ua.sb.repositories;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Serhii Buria
 */
public interface MainService {
    void processTextMessage(Update update);
}
