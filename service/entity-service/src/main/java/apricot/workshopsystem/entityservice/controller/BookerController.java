package apricot.workshopsystem.entityservice.controller;

import apricot.workshopsystem.common.dto.BookerDTO;
import apricot.workshopsystem.common.exception.BadRequestException;
import apricot.workshopsystem.common.webutil.HeaderUtil;
import apricot.workshopsystem.common.webutil.PaginationUtil;
import apricot.workshopsystem.common.webutil.ResponseUtil;
import apricot.workshopsystem.entityservice.model.dao.Booker;
import apricot.workshopsystem.entityservice.service.BookerService;
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
@Api(value = "booker-service")
public class BookerController {
    private static final Logger LOG = LoggerFactory.getLogger(BookerController.class);
    private static final String ENTITY_NAME = "booker";
    private static final String APPLICATION_NAME = "booker-service";

    @Autowired
    private BookerService bookerService;

    /**
     * {@code GET /api/bookers/:id} : Get the "id" booker.
     *
     * @param id The id of the bookerDTO to retrieve.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookers/{id}")
    @ApiOperation(value = "Search a booker with an ID", response = Booker.class)
    public ResponseEntity<BookerDTO> get(@PathVariable(value = "id") Long id) {
        LOG.info("REST request to get Booker : {}", id);

        Optional<BookerDTO> bookerDTO = bookerService.find(id);

        return ResponseUtil.wrapOrNotFound(bookerDTO);
    }

    /**
     * {@code GET /api/bookers} : Get all the bookers.
     *
     * @param pageable The pagination information.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookers in body.
     */
    @GetMapping("/bookers")
    @ApiOperation(value = "List all bookers", response = Booker.class, responseContainer = "List")
    public ResponseEntity<List<BookerDTO>> getAll(final Pageable pageable) {
        LOG.info("REST request to get all Bookers");
        
        Page<BookerDTO> page = bookerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code POST /bookers} : Create a new booker.
     *
     * @param bookerDTO The bookerDTO to create.
     *
     * @return The {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookerDTO,
     *      or with status {@code 400 (Bad Request)} if the booker has already an ID.
     *
     * @throws BadRequestException if the requested data is invalid.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bookers")
    @ApiOperation(value = "Add the booker")
    public ResponseEntity<BookerDTO> add(@Valid @RequestBody BookerDTO bookerDTO) throws BadRequestException, URISyntaxException {
        LOG.info("REST request to save Booker : {}", bookerDTO);

        if (bookerDTO.getId() != null) {
            throw new BadRequestException("Id must be null to create a new Booker", ENTITY_NAME, "Id is not null");
        }

        BookerDTO result = bookerService.save(bookerDTO);
        return ResponseEntity.created(new URI("/api/bookers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(APPLICATION_NAME, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT /bookers} : Updates an existing booker.
     *
     * @param bookerDTO The bookerDTO to update.
     *
     * @return The {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookerDTO,
     *      or with status {@code 400 (Bad Request)} if the bookerDTO is not valid,
     *      or with status {@code 500 (Internal Server Error)} if the bookerDTO couldn't be updated.
     *
     * @throws BadRequestException if the requested data is invalid.
     */
    @PutMapping("/bookers")
    @ApiOperation(value = "Update the booker")
    public ResponseEntity<?> update(@Valid @RequestBody BookerDTO bookerDTO) throws BadRequestException {
        LOG.info("REST request to update Booker : {}", bookerDTO);

        if (bookerDTO.getId() == null) {
            throw new BadRequestException("Id must exists (got null value) to update the booker", ENTITY_NAME, "Id is null");
        }

        bookerService.find(bookerDTO.getId())
                .orElseThrow(() -> new BadRequestException("No Booker found with id " + bookerDTO.getId(), ENTITY_NAME, "Id not found"));

        BookerDTO result = bookerService.save(bookerDTO);

        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(APPLICATION_NAME, true, ENTITY_NAME, bookerDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code DELETE /bookers/:id} : Delete the "id" booker.
     *
     * @param id The id of the bookerDTO to delete.
     * @return The {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bookers/{id}")
    @ApiOperation(value = "Delete the booker")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        LOG.info("REST request to delete Booker : {}", id);

        bookerService.delete(id);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(APPLICATION_NAME, true, ENTITY_NAME, id.toString())).build();
    }
}
