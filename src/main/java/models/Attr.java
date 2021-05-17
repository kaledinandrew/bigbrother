package models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "attrs")
@Data
public class Attr extends BaseEntity{
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "attrs", fetch = FetchType.LAZY)
    private List<User> users;

    public Attr() {
        super.status = Status.ACTIVE;
    }

    public Attr(String name) {
        this.name = name;
        super.status = Status.ACTIVE;
        super.created = Date.valueOf(LocalDate.now());
        super.updated = Date.valueOf(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Attr{" +
                "id: " + super.getId() + ", " +
                "name: " + name + "}";
    }
}
