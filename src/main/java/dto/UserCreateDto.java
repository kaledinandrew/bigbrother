package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import models.Role;
import models.Status;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import repositories.RoleRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateDto {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String role;

    public User toUser(Role role) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        user.setRoles(new HashSet<>());
        user.setAttrs(new HashSet<>());
        user.setScripts(new HashSet<>());
        user.getRoles().add(role);

        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        return user;
    }
}
