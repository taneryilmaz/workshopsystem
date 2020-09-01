package apricot.workshopsystem.entityservice.service;

import apricot.workshopsystem.common.dto.RoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link apricot.workshopsystem.entityservice.model.dao.Room}.
 */
public interface RoomService {

    /**
     * Get the "id" room.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomDTO> find(final Long id);

    /**
     * Get all the rooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RoomDTO> findAll(final Pageable pageable);

    /**
     * Save a room.
     *
     * @param roomDTO the entity to save.
     * @return the persisted entity.
     */
    RoomDTO save(final RoomDTO roomDTO);

    /**
     * Delete the "id" room.
     *
     * @param id the id of the entity.
     */
    void delete(final Long id);
}
