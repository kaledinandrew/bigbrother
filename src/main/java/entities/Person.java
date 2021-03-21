package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

@Entity
public class Person {
    private @Id @GeneratedValue Long id;
    // TODO: private password (?)
    private String firstName;
    private String lastName;
    private String patronymic;
    private ArrayList<String> roles;
    private ArrayList<Long> scriptsId;

    public Person() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public ArrayList<Long> getScriptsId() {
        return scriptsId;
    }

    public void setScriptsId(ArrayList<Long> scriptsId) {
        this.scriptsId = scriptsId;
    }
}
