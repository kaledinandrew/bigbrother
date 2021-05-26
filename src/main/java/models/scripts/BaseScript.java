package models.scripts;

import dto.scripts.BaseScriptDto;
import dto.scripts.CountContactScriptDto;
import dto.scripts.TimeIntervalContactScriptDto;
import lombok.Data;
import models.BaseEntity;
import models.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "script_type", discriminatorType = DiscriminatorType.INTEGER)
@Data
public class BaseScript extends BaseEntity {

    @Column(name = "script_name")
    protected String scriptName;

    @Column(name = "id1")
    protected Long id1;

    @Column(name = "id2")
    protected Long id2;

    @ManyToMany(mappedBy = "scripts")
    private Set<User> users;

    public void addUser(User user) {
        if (!users.contains(user)) {
            this.users.add(user);
        }
    }

    public void removeUser(User user) {
        if (users.contains(user)) {
            this.users.remove(user);
        }
    }

    public static BaseScriptDto toChild(BaseScript script) {
        if (script instanceof CountContactScript) {
            return CountContactScriptDto.fromCountContactScript((CountContactScript) script);
        } else if (script instanceof TimeIntervalContactScript) {
            return TimeIntervalContactScriptDto.fromTimeIntervalContactScript((TimeIntervalContactScript) script);
        }
        return null;
    }
}
