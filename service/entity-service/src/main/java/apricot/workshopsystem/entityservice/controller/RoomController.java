package apricot.workshopsystem.entityservice.controller;

import apricot.workshopsystem.common.dto.RoomDTO;
import apricot.workshopsystem.common.webutil.HeaderUtil;
import apricot.workshopsystem.common.webutil.PaginationUtil;
import apricot.workshopsystem.common.webutil.ResponseUtil;
import apricot.workshopsystem.entityservice.model.dao.Room;
import apricot.workshopsystem.entityservice.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(value = "room-service")
public class RoomController {
    private static final Logger LOG = LoggerFactory.getLogger(RoomController.class);
    private static final String ENTITY_NAME = "room";
    private static final String APPLICATION_NAME = "room-service";

    @Autowired
    private RoomService roomService;

    /**
     * {@code GET /api/rooms} : Get all the rooms.
     *
     * @param pageable The pagination information.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and the list of rooms in body.
     */
    @GetMapping("/rooms")
    @ApiOperation(value = "List all rooms", response = Room.class, responseContainer = "List")
    public ResponseEntity<List<RoomDTO>> getAll(final Pageable pageable) {
        LOG.info("REST request to get all Rooms");

        Page<RoomDTO> page = roomService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET /api/rooms/:id} : Get the "id" room.
     *
     * @param id The id of the roomDTO to retrieve.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rooms/{id}")
    @ApiOperation(value = "Search a room with an ID", response = Room.class)
    public ResponseEntity<RoomDTO> get(@PathVariable(value = "id") Long id) {
        LOG.info("REST request to get Room: {}", id);

        Optional<RoomDTO> roomDTO = roomService.find(id);

        return ResponseUtil.wrapOrNotFound(roomDTO);
    }

    /**
     * {@code POST /rooms} : Create a new room.
     *
     * @param roomDTO The roomDTO to create.
     *
     * @return The {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomDTO,
     *      or with status {@code 400 (Bad Request)} if the room has already an ID.
     *
     * @throws ResponseStatusException if the requested data is invalid.
     */
    @PostMapping("/rooms")
    @ApiOperation(value = "Add the room")
    public ResponseEntity<RoomDTO> add(@Valid @RequestBody RoomDTO roomDTO) throws ResponseStatusException, URISyntaxException {
        LOG.info("REST request to save Room: {}", roomDTO);

        if (roomDTO.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be null to create a new Room: " + roomDTO);
        }

        RoomDTO result = roomService.save(roomDTO);

        return ResponseEntity.created(new URI("/api/rooms/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT /rooms} : Updates an existing room.
     *
     * @param roomDTO The roomDTO to update.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomDTO,
     *      or with status {@code 400 (Bad Request)} if the roomDTO is not valid,
     *      or with status {@code 500 (Internal Server Error)} if the roomDTO couldn't be updated.
     *
     * @throws ResponseStatusException if the requested data is invalid.
     */
    @PutMapping("/rooms")
    @ApiOperation(value = "Update the room")
    public ResponseEntity<?> update(@Valid @RequestBody RoomDTO roomDTO) throws ResponseStatusException {
        LOG.info("REST request to update Room : {}", roomDTO);

        if (roomDTO.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is null: " + roomDTO);
        }

        roomService.find(roomDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room not found with id: " + roomDTO.getId()));

        RoomDTO result = roomService.save(roomDTO);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, roomDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code DELETE /rooms/:id} : Delete the "id" room.
     *
     * @param id The id of the roomDTO to delete.
     *
     * @return The {@link ResponseEntity} with status {@code 204 (NO_CONTENT)} if deleted,
     *      or with status {@code 400 (BAD_REQUEST)} if error occurs.
     */
    @DeleteMapping("/rooms/{id}")
    @ApiOperation(value = "Delete the room")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        LOG.info("REST request to delete Room : {}", id);

        roomService.find(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room not found with id: " + id));

        roomService.delete(id);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }
}
