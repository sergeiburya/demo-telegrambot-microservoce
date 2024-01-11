package ua.sb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.sb.model.BinaryContent;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {
}
