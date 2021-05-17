package repositories;

import models.Attr;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttrRepository extends JpaRepository<Attr, Long> {
    Attr findByName(String name);
}
