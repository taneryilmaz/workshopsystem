package apricot.workshopsystem.entityservice.controller;

import apricot.workshopsystem.common.dto.ReservationDTO;
import apricot.workshopsystem.common.exception.BadRequestException;
import apricot.workshopsystem.common.webutil.HeaderUtil;
import apricot.workshopsystem.common.webutil.PaginationUtil;
import apricot.workshopsystem.common.webutil.ResponseUtil;
import apricot.workshopsystem.entityservice.model.dao.Reservation;
import apricot.workshopsystem.entityservice.service.ReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(value = "reservation-service")
public class ReservationController {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);
    private static final String ENTITY_NAME = "reservation";
    private static final String APPLICATION_NAME = "reservation-service";

    @Autowired
    private ReservationService reservationService;

    /**
     * {@code GET /api/reservations/:id} : Get the "id" reservation.
     *
     * @param id The id of the reservationDTO to retrieve.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reservations/{id}")
    @ApiOperation(value = "Search a reservation with an ID", response = Reservation.class)
    public ResponseEntity<ReservationDTO> get(@PathVariable(value = "id") Long id) {
        LOG.info("REST request to get Reservation : {}", id);

        Optional<ReservationDTO> reservationDTO = reservationService.find(id);

        return ResponseUtil.wrapOrNotFound(reservationDTO);
    }

    /**
     * {@code GET /api/reservations} : Get all the reservations.
     *
     * @param pageable The pagination information.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservations in body.
     */
    @GetMapping("/reservations")
    @ApiOperation(value = "List all reservations", response = Reservation.class, responseContainer = "List")
    public ResponseEntity<List<ReservationDTO>> getAll(final Pageable pageable) {
        LOG.info("REST request to get all Reservations");

        Page<ReservationDTO> page = reservationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code POST /reservations} : Create a new reservation.
     *
     * @param reservationDTO the reservationDTO to create.
     *
     * @return The {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservationDTO,
     *      or with status {@code 400 (Bad Request)} if the reservation has already an ID.
     *
     * @throws BadRequestException if the requested data is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reservations")
    @ApiOperation(value = "Add the reservation")
    public ResponseEntity<ReservationDTO> add(@Valid @RequestBody ReservationDTO reservationDTO) throws BadRequestException, URISyntaxException {
        LOG.info("REST request to save Reservation : {}", reservationDTO);

        if (reservationDTO.getId() != null) {
            throw new BadRequestException("Id must be null to create a new Reservation", ENTITY_NAME, "Id is not null");
        }

        ReservationDTO result = reservationService.save(reservationDTO);
        return ResponseEntity.created(new URI("/api/reservations/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT /reservations} : Updates an existing reservation.
     *
     * @param reservationDTO The reservationDTO to update.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservationDTO,
     *      or with status {@code 400 (Bad Request)} if the reservationDTO is not valid,
     *      or with status {@code 500 (Internal Server Error)} if the reservationDTO couldn't be updated.
     *
     * @throws BadRequestException if the requested data is invalid.
     */
    @PutMapping("/reservations")
    @ApiOperation(value = "Update the reservation")
    public ResponseEntity<?> update(@Valid @RequestBody ReservationDTO reservationDTO) throws BadRequestException {
        LOG.info("REST request to update Reservation : {}", reservationDTO);

        if (reservationDTO.getId() == null) {
            throw new BadRequestException("Id must exists (got null value) to update the reservation", ENTITY_NAME, "Id is null");
        }

        reservationService.find(reservationDTO.getId())
                .orElseThrow(() -> new BadRequestException("No Reservation found with id " + reservationDTO.getId(), ENTITY_NAME, "Id not found"));

        ReservationDTO result = reservationService.save(reservationDTO);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, reservationDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code DELETE /reservations/:id} : Delete the "id" reservation.
     *
     * @param id The id of the reservationDTO to delete.
     * @return The {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reservations/{id}")
    @ApiOperation(value = "Delete the reservation")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        LOG.info("REST request to delete Reservation : {}", id);

        reservationService.delete(id);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }
}
