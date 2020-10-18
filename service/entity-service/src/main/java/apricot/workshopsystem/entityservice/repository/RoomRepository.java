package apricot.workshopsystem.entityservice.repository;

import apricot.workshopsystem.entityservice.model.dao.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
