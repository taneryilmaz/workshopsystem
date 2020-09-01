package apricot.workshopsystem.entityservice.repository;

import apricot.workshopsystem.entityservice.model.dao.Booker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookerRepository extends JpaRepository<Booker, Long> {
    List<Booker> findByName(String name);
}
