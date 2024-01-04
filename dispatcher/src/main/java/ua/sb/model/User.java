package ua.sb.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.ToString;

/**
 * @author Serhii Buria
 */
@Data
@Entity(name = "users")
@ToString
public class User {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
}
