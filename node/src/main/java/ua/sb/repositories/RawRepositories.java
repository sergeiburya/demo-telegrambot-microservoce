package ua.sb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.sb.model.RawData;

/**
 * @author Serhii Buria
 */
@Repository
public interface RawRepositories extends JpaRepository<RawData, Long> {
}
