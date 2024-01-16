package ua.sb.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.sb.model.AppUser;

@Repository
public interface AppUserRepositories extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByTelegramUserId(Long id);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

}
