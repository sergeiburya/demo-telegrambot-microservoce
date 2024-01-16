package ua.sb.service.impl;

import static ua.sb.model.UserState.BASIC_STATE;
import static ua.sb.model.UserState.WAIT_FOR_EMAIL_STATE;
import static ua.sb.model.enums.ServiceCommand.CANCEL;
import static ua.sb.model.enums.ServiceCommand.HELP;
import static ua.sb.model.enums.ServiceCommand.REGISTRATION;
import static ua.sb.model.enums.ServiceCommand.START;

import java.util.Optional;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.sb.exception.UploadFileException;
import ua.sb.model.AppDocument;
import ua.sb.model.AppPhoto;
import ua.sb.model.AppUser;
import ua.sb.model.RawData;
import ua.sb.model.UserState;
import ua.sb.model.enums.LinkType;
import ua.sb.model.enums.ServiceCommand;
import ua.sb.repositories.AppUserRepositories;
import ua.sb.repositories.RawRepositories;
import ua.sb.service.AppUserService;
import ua.sb.service.FileService;
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
    private final FileService fileService;
    private final AppUserService appUserService;

    public MainServiceImpl(RawRepositories rawRepositories,
                           ProducerService producerService,
                           AppUserRepositories appUserRepositories,
                           FileService fileService, AppUserService appUserService) {
        this.rawRepositories = rawRepositories;
        this.producerService = producerService;
        this.appUserRepositories = appUserRepositories;
        this.fileService = fileService;
        this.appUserService = appUserService;
    }

    @Transactional
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        UserState userState = appUser.getState();
        String text = update.getMessage().getText();
        String output = "";

        ServiceCommand serviceCommand = ServiceCommand.fromValue(text);
        if (CANCEL.equals(serviceCommand)) {
            output = cancelProcess(appUser);
        } else if (BASIC_STATE.equals(userState)) {
            output = processServiceCommand(appUser, text);
        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
            output = appUserService.setEmail(appUser, text);
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

        try {
            AppDocument doc = fileService.processDoc(update.getMessage());
            String link = fileService.generateLink(doc.getId(), LinkType.GET_DOC);
            String answer = "Document uploaded successfully! Download link: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "File upload failed. Try later.";
            sendAnswer(error, chatId);
        }
    }

    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        AppUser appUser = findOrSaveAppUser(update);
        Long chatId = update.getMessage().getChatId();
        if (isNotAllowToSendContent(chatId, appUser)) {
            return;
        }
        try {
            AppPhoto photo = fileService.processPhoto(update.getMessage());
            String link = fileService.generateLink(photo.getId(), LinkType.GET_PHOTO);
            String answer = "Photo uploaded successfully! Download link: " + link;
            sendAnswer(answer, chatId);
        } catch (UploadFileException e) {
            log.error(e);
            String error = "File upload failed. Try later.";
            sendAnswer(error, chatId);
        }
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
            return true;
        }
        return false;
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        ServiceCommand serviceCommand = ServiceCommand.fromValue(cmd);
        if (REGISTRATION.equals(serviceCommand)) {
            return appUserService.registerUser(appUser);
        } else if (HELP.equals(serviceCommand)) {
            return help();
        } else if (START.equals(serviceCommand)) {
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
        Optional<AppUser> optionalPersistentAppUser =
                appUserRepositories.findByTelegramUserId(telegramUser.getId());
        if (optionalPersistentAppUser.isEmpty()) {
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .userName(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .isActive(false)
                    .state(BASIC_STATE)
                    .build();
            return appUserRepositories.save(transientAppUser);
        }
        return optionalPersistentAppUser.get();
    }
}
