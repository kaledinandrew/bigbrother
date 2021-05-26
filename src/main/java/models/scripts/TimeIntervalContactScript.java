package models.scripts;

import dto.scripts.TimeIntervalContactScriptDto;
import lombok.Data;
import models.User;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashSet;

@Entity
@DiscriminatorValue("2")
@Data
public class TimeIntervalContactScript extends BaseScript {

    @Column(name = "time_from")
    private Long from;

    @Column(name = "time_to")
    private Long to;

    @Column(name = "success")
    private Boolean success;

    public TimeIntervalContactScript() {}

    public TimeIntervalContactScript(String scriptName, Long id1, Long id2, Long from, Long to) {
        super.scriptName = scriptName;
        super.id1 = id1;
        super.id2 = id2;
        this.from = from;
        this.to = to;
        this.success = false;
    }
    public TimeIntervalContactScript(TimeIntervalContactScriptDto dto, User u1, User u2) {
        super.scriptName = dto.getScriptName();
        super.id1 = dto.getId1();
        super.id2 = dto.getId2();
        this.from = dto.getFrom();
        this.to = dto.getTo();
        this.success = dto.getSuccess();
        super.setUsers(new HashSet<>());
        if (u1 != null) {
            super.addUser(u1);
        }
        if (u2 != null) {
            super.addUser(u2);
        }
    }

    @Override
    public String toString() {
        return "TimeIntervalContactScript{" +
                "id=" + id +
                ", scriptName='" + scriptName + '\'' +
                ", id1=" + id1 +
                ", id2=" + id2 +
                ", from=" + from +
                ", to=" + to +
                ", success=" + success +
                '}';
    }

}
