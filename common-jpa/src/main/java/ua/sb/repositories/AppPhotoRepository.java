package ua.sb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.sb.model.AppPhoto;

@Repository
public interface AppPhotoRepository extends JpaRepository<AppPhoto, Integer> {
}
