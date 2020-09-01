package apricot.workshopsystem.entityservice.service;

import apricot.workshopsystem.common.dto.BookerDTO;
import apricot.workshopsystem.entityservice.mapper.BookerMapper;
import apricot.workshopsystem.entityservice.model.dao.Booker;
import apricot.workshopsystem.entityservice.repository.BookerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class BookerServiceImpl implements BookerService {
    private static final Logger LOG = LoggerFactory.getLogger(BookerServiceImpl.class);
    private final BookerRepository bookerRepository;
    private final BookerMapper bookerMapper;

    public BookerServiceImpl(BookerRepository bookerRepository, BookerMapper bookerMapper) {
        this.bookerRepository = bookerRepository;
        this.bookerMapper = bookerMapper;
    }

    /**
     * Get one booker by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BookerDTO> find(final Long id) {
        LOG.debug("Request to get Booker : {}", id);

        return bookerRepository.findById(id).map(bookerMapper::toDto);
    }

    /**
     * Get all the bookers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookerDTO> findAll(final Pageable pageable) {
        LOG.debug("Request to get all Bookers");

        return bookerRepository.findAll(pageable).map(bookerMapper::toDto);
    }

    /**
     * Save a booker.
     *
     * @param bookerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BookerDTO save(final BookerDTO bookerDTO) {
        LOG.debug("Request to save Booker : {}", bookerDTO);

        Booker booker = bookerMapper.toEntity(bookerDTO);
        booker = bookerRepository.save(booker);

        return bookerMapper.toDto(booker);
    }

    /**
     * Delete the booker by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(final Long id) {
        LOG.debug("Request to delete Booker : {}", id);

        bookerRepository.deleteById(id);
    }
}
