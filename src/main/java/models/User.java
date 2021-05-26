package models;

import dto.UserCreateDto;
import lombok.Data;
import models.scripts.BaseScript;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_attrs",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "attr_id", referencedColumnName = "id")})
    private Set<Attr> attrs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_scripts",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "script_id", referencedColumnName = "id")})
    private Set<BaseScript> scripts;

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

    public void addAttr(Attr toAdd) {
        if (!attrs.contains(toAdd)) {
            this.attrs.add(toAdd);
        }
    }

    public void removeAttr(Attr attr) {
        if (attrs.contains(attr)) {
            this.attrs.remove(attr);
        }
    }

    public void addScript(BaseScript toAdd) {
        this.scripts.add(toAdd);
    }

    public void removeScript(BaseScript script) {
        if (scripts.contains(script)) {
            this.scripts.remove(script);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
