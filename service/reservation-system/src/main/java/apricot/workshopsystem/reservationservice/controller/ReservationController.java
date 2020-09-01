package apricot.workshopsystem.reservationservice.controller;

import apricot.workshopsystem.common.dto.BookerDTO;
import apricot.workshopsystem.common.dto.ReservationDTO;
import apricot.workshopsystem.common.dto.RoomDTO;
import apricot.workshopsystem.common.exception.BadRequestException;
import apricot.workshopsystem.common.util.TimeUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(value = "reservation-service")
public class ReservationController {
    private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);
    private static final String ENTITY_NAME = "reservation";
    private static final String APPLICATION_NAME = "reservation-service";
    private static final String BOOKERS_GET_URL = "http://entity-service/api/bookers";
    private static final String ROOMS_GET_URL = "http://entity-service/api/rooms";
    private static final String RESERVATION_CREATE_URL = "http://entity-service/api/reservations";
    private HashMap<Long, TreeSet<ReservationContainer>> roomReservations = new HashMap<>();

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/reservations/reserve")
    public ResponseEntity<?> reserve(@Valid @RequestBody ReservationDTO request) throws BadRequestException {
        if (request.getStartDateTime().isAfter(request.getEndDateTime())) {
            return new ResponseEntity<>("StartDatetime must be before EndDatetime", HttpStatus.BAD_REQUEST);
        }

        if (overlapsWithReservations(roomReservations.get(request.getRoomId()), request)) {
            return new ResponseEntity<>("Reservation conflict", HttpStatus.BAD_REQUEST);
        }

        BookerDTO[] bookers = restTemplate.getForObject(BOOKERS_GET_URL, BookerDTO[].class);
        Map<Long, BookerDTO> bookersMap = (bookers == null) ?
                Collections.emptyMap() : Arrays.stream(bookers).collect(Collectors.toMap(BookerDTO::getId, e -> e));

        RoomDTO[] rooms = restTemplate.getForObject(ROOMS_GET_URL, RoomDTO[].class);
        Map<Long, RoomDTO> roomsMap = (rooms == null) ?
                Collections.emptyMap() : Arrays.stream(rooms).collect(Collectors.toMap(RoomDTO::getId, e -> e));

        if (bookersMap.get(request.getBookerId()) == null) {
            throw new BadRequestException("Booker for request not exists: " + request, ENTITY_NAME, "Object not found");
        }

        if (roomsMap.get(request.getRoomId()) == null) {
            throw new BadRequestException("Room for request not exists: " + request, ENTITY_NAME, "Object not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<ReservationDTO> httpRequest = new HttpEntity<>(request, headers);

        ResponseEntity<ReservationDTO> response = restTemplate.postForEntity(RESERVATION_CREATE_URL, httpRequest, ReservationDTO.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOG.info("Created reservation: " + response.getBody());
        } else {
            LOG.error("Reservation creation failed: " + response.getStatusCode());
            throw new BadRequestException("Reservation creation failed for: " + request, ENTITY_NAME, "Request failed");
        }

        TreeSet<ReservationContainer> reservations = roomReservations.computeIfAbsent(request.getRoomId(), k -> new TreeSet<>());
        reservations.add(new ReservationContainer(request));

        return new ResponseEntity<>(request, new HttpHeaders(), HttpStatus.OK);
    }

    private boolean overlapsWithReservations(final TreeSet<ReservationContainer> reservations, final ReservationDTO request) {
        if (reservations == null) {
            return false;
        }

        for (ReservationContainer reservationContainer : reservations) {
            ReservationDTO r = reservationContainer.getReservation();
            if (TimeUtil.overlaps(r.getStartDateTime(), r.getEndDateTime(), request.getStartDateTime(), request.getEndDateTime())) {
                return true;
            }
        }

        return false;
    }
}
