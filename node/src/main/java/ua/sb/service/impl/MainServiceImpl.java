package ua.sb.service.impl;

import static ua.sb.model.UserState.BASIC_STATE;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.sb.model.AppUser;
import ua.sb.model.RawData;
import ua.sb.repositories.AppUserRepositories;
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
    private final AppUserRepositories appUserRepositories;

    public MainServiceImpl(RawRepositories rawRepositories,
                           ProducerService producerService,
                           AppUserRepositories appUserRepositories) {
        this.rawRepositories = rawRepositories;
        this.producerService = producerService;
        this.appUserRepositories = appUserRepositories;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        Message message = update.getMessage();
        User telegramUser = message.getFrom();
        AppUser appUser = findOrSaveAppUser(telegramUser);
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

    private AppUser findOrSaveAppUser(User telegramUser) {
        AppUser persistentAppUser =
                appUserRepositories.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserRepositories.save(transientAppUser);
        }
        return persistentAppUser;
    }
}
