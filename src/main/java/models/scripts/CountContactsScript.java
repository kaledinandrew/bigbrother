package models.scripts;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("1")
@Data
public class CountContactsScript extends BaseScript {

    @Column(name = "script_name")
    private String scriptName;

    @Column(name = "id1")
    private Long id1;

    @Column(name = "id2")
    private Long id2;

    @Column(name = "count")
    private Long count;

    public CountContactsScript() {}

    public CountContactsScript(String scriptName, Long id1, Long id2) {
        this.scriptName = scriptName;
        this.id1 = id1;
        this.id2 = id2;
        this.count = 0L;
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
}
