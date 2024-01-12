package ua.sb.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ua.sb.model.AppDocument;
import ua.sb.model.AppPhoto;
import ua.sb.model.enums.LinkType;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);

    AppPhoto processPhoto(Message telegramMessage);

    String generateLink(Long docId, LinkType linkType);
}
