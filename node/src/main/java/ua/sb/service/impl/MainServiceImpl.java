package ua.sb.service.impl;

import static ua.sb.model.UserState.BASIC_STATE;
import static ua.sb.model.UserState.WAIT_FOR_EMAIL_STATE;
import static ua.sb.model.enums.ServiceCommands.CANCEL;
import static ua.sb.model.enums.ServiceCommands.HELP;
import static ua.sb.model.enums.ServiceCommands.REGISTRATION;
import static ua.sb.model.enums.ServiceCommands.START;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.sb.model.AppUser;
import ua.sb.model.RawData;
import ua.sb.model.UserState;
import ua.sb.repositories.AppUserRepositories;
import ua.sb.repositories.RawRepositories;
import ua.sb.service.MainService;
import ua.sb.service.ProducerService;

/**
 * @author Serhii Buria
 */
@Service
@Log4j
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
        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String cmd = update.getMessage().getText();
        String output = "";

        if (CANCEL.equals(cmd)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, cmd);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            //TODO
        } else {
            log.error("Unknown user state: " + userState);
            output = "Unknown error. Enter /cancel and try again!";
        }

        Long chatId = update.getMessage().getChatId();
        sendAnswer(output, chatId);
    }

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        //TODO add save doc method
        String answer = "Document uploaded successfully! Download link: http:/ua.sb/getDoc/..";
        sendAnswer(answer, chatId);
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        //TODO add save photo method
        String answer = "Photo uploaded successfully! Download link: http:/ua.sb/getPhoto/..";
        sendAnswer(answer, chatId);
    }

    private boolean isNotAllowToSendContent(Long chatId, AppUser appUser) {
        UserState userState = appUser.getState();
        if (!appUser.getIsActive()) {
            String errorMessage = "Register or activate an account to download content!";
            sendAnswer(errorMessage, chatId);
            return true;
        } else if (!BASIC_STATE.equals(userState)) {
            String errorMessage = "Cancel the command. Enter /cancel to send files";
            sendAnswer(errorMessage, chatId);
        }
        return false;
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        if (REGISTRATION.equals(cmd)) {
            //TODO add registration
            return "The command is temporarily unavailable!";
        } else if (HELP.equals(cmd)) {
            return help();
        } else if (START.equals(cmd)) {
            return "Hello! To view available commands, enter /help";
        }

        return "Unknown command! To view available commands, enter /help";
    }

    private String help() {
        return "List of available commands: \n"
                + "- /registration - user registration\n"
                + "- /cancel - cancel execution of the current command";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserRepositories.save(appUser);
        return "Command canceled";
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawRepositories.save(rawData);
    }

    private AppUser findOrSaveAppUser(Update update) {
        User telegramUser = update.getMessage().getFrom();
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
