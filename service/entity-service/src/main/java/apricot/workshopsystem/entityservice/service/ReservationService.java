package apricot.workshopsystem.entityservice.service;

import apricot.workshopsystem.common.dto.ReservationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link apricot.workshopsystem.entityservice.model.dao.Reservation}.
 */
public interface ReservationService {

    /**
     * Get the "id" reservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservationDTO> find(final Long id);

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReservationDTO> findAll(final Pageable pageable);

    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    ReservationDTO save(final ReservationDTO reservationDTO);

    /**
     * Delete the "id" reservation.
     *
     * @param id the id of the entity.
     */
    void delete(final Long id);
}
