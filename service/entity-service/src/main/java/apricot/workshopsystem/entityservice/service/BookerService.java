package apricot.workshopsystem.entityservice.service;

import apricot.workshopsystem.common.dto.BookerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link apricot.workshopsystem.entityservice.model.dao.Booker}.
 */
public interface BookerService {

    /**
     * Get the "id" booker.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookerDTO> find(final Long id);

    /**
     * Get all the bookers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookerDTO> findAll(final Pageable pageable);

    /**
     * Save a booker.
     *
     * @param bookerDTO the entity to save.
     * @return the persisted entity.
     */
    BookerDTO save(final BookerDTO bookerDTO);

    /**
     * Delete the "id" booker.
     *
     * @param id the id of the entity.
     */
    void delete(final Long id);
}
