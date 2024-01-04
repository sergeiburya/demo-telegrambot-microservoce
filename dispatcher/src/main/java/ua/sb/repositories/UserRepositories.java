package ua.sb.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.sb.model.User;

/**
 * @author Serhii Buria
 */
@Repository
public interface UserRepositories extends CrudRepository<User, Long> {
}
