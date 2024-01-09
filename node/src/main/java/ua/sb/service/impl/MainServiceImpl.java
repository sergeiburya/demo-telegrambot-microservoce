package ua.sb.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.sb.model.RawData;
import ua.sb.repositories.MainService;
import ua.sb.repositories.RawRepositories;
import ua.sb.service.ProducerService;

/**
 * @author Serhii Buria
 */
@Service
public class MainServiceImpl implements MainService {
    private final RawRepositories rawRepositories;
    private final ProducerService producerService;

    public MainServiceImpl(RawRepositories rawRepositories, ProducerService producerService) {
        this.rawRepositories = rawRepositories;
        this.producerService = producerService;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Hello! Node: text message");
        producerService.producerAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawRepositories.save(rawData);
    }
}
