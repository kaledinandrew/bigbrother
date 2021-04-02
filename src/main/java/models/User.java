package models;

import dto.UserCreateDto;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity{

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    public void editUser(UserCreateDto dto) {
        if (!username.equals(dto.getUsername())) {
            this.username = dto.getUsername();
        }
        if (!firstName.equals(dto.getFirstName())) {
            this.firstName = dto.getFirstName();
        }
        if (!lastName.equals(dto.getLastName())) {
            this.lastName = dto.getLastName();
        }
        this.password = new BCryptPasswordEncoder().encode(dto.getPassword());
        this.updated = new Date();
    }
}
