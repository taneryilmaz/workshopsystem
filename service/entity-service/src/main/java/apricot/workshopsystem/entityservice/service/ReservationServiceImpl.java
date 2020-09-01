package apricot.workshopsystem.entityservice.service;

import apricot.workshopsystem.common.dto.ReservationDTO;
import apricot.workshopsystem.entityservice.mapper.ReservationMapper;
import apricot.workshopsystem.entityservice.model.dao.Reservation;
import apricot.workshopsystem.entityservice.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    /**
     * Get one reservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationDTO> find(final Long id) {
        LOG.debug("Request to get Reservation : {}", id);

        return reservationRepository.findById(id).map(reservationMapper::toDto);
    }

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservationDTO> findAll(final Pageable pageable) {
        LOG.debug("Request to get all Reservations");

        return reservationRepository.findAll(pageable).map(reservationMapper::toDto);
    }

    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ReservationDTO save(final ReservationDTO reservationDTO) {
        LOG.debug("Request to save Reservation : {}", reservationDTO);

        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = reservationRepository.save(reservation);

        return reservationMapper.toDto(reservation);
    }

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(final Long id) {
        LOG.debug("Request to delete Reservation : {}", id);

        reservationRepository.deleteById(id);
    }
}
