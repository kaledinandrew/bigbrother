package repositories;

import models.scripts.BaseScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseScriptRepository extends JpaRepository<BaseScript, Long> {
}
