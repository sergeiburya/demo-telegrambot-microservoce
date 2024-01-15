package ua.sb.service;

import ua.sb.model.AppDocument;
import ua.sb.model.AppPhoto;

public interface FileService {

    AppDocument getDocument(String id);

    AppPhoto getPhoto(String id);
}
