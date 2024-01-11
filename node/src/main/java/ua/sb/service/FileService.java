package ua.sb.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ua.sb.model.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
