package ua.sb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.sb.model.AppDocument;

@Repository
public interface AppDocumentRepository extends JpaRepository<AppDocument, Long> {
}
