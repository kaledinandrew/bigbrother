package models.scripts;

import dto.scripts.CountContactScriptDto;
import lombok.Data;
import models.User;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;

@Entity
@DiscriminatorValue("1")
@Data
public class CountContactScript extends BaseScript {

    @Column(name = "count")
    private Long count;

    public CountContactScript() {}

    public CountContactScript(String scriptName, Long id1, Long id2) {
        super.scriptName = scriptName;
        super.id1 = id1;
        super.id2 = id2;
        this.count = 0L;
    }

    public CountContactScript(CountContactScriptDto dto, User u1, User u2) {
        this.scriptName = dto.getScriptName();
        this.id1 = dto.getId1();
        this.id2 = dto.getId2();
        this.count = 0L;
        super.setUsers(new HashSet<>());
        if (u1 != null) {
            super.addUser(u1);
        }
        if (u2 != null) {
            super.addUser(u2);
        }
    }

    public void incrementCount() {
        this.count++;
    }

    @Override
    public String toString() {
        return "CountContactsScript{" +
                "id=" + id +
                ", scriptName='" + scriptName + '\'' +
                ", id1=" + id1 +
                ", id2=" + id2 +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountContactScript)) return false;
        if (!super.equals(o)) return false;
        CountContactScript script = (CountContactScript) o;
        return Objects.equals(count, script.count) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), count);
    }
}
