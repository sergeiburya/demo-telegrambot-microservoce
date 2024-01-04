package ua.sb.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import lombok.Data;
import lombok.ToString;

@Data
@Entity(name="users")
@ToString
public class User {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
}