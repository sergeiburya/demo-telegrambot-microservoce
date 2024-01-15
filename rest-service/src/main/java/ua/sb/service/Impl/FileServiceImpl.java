package ua.sb.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import ua.sb.model.AppDocument;
import ua.sb.model.AppPhoto;
import ua.sb.repositories.AppDocumentRepository;
import ua.sb.repositories.AppPhotoRepository;
import ua.sb.service.FileService;
import ua.sb.utils.CryptoTool;

@Log4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final AppDocumentRepository appDocumentRepository;

    private final AppPhotoRepository appPhotoRepository;

    private final CryptoTool cryptoTool;

    @Override
    public AppDocument getDocument(String hash) {
        var id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appDocumentRepository.findById(id).orElse(null);
    }

    @Override
    public AppPhoto getPhoto(String hash) {
        Long id = cryptoTool.idOf(hash);
        if (id == null) {
            return null;
        }
        return appPhotoRepository.findById(Math.toIntExact(id)).orElse(null);
    }
}
