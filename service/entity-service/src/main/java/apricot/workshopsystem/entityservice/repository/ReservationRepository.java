package apricot.workshopsystem.entityservice.repository;

import apricot.workshopsystem.entityservice.model.dao.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
