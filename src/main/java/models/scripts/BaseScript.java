package models.scripts;

import dto.scripts.BaseScriptDto;
import dto.scripts.CountContactScriptDto;
import dto.scripts.TimeIntervalContactScriptDto;
import lombok.Data;
import models.BaseEntity;
import models.User;

import javax.persistence.*;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseScript script = (BaseScript) o;
        return Objects.equals(scriptName, script.scriptName) &&
                Objects.equals(id1, script.id1) &&
                Objects.equals(id2, script.id2)  &&
                Objects.equals(super.id, script.getId()) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), super.id, scriptName, id1, id2);
    }
}
