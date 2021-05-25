package models.scripts;

import models.BaseEntity;
import models.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "script_type", discriminatorType = DiscriminatorType.INTEGER)
public class BaseScript extends BaseEntity {

    @ManyToMany(mappedBy = "scripts")
    private Set<User> users;


}
