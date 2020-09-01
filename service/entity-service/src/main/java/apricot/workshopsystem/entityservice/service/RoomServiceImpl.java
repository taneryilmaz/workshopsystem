package apricot.workshopsystem.entityservice.service;

import apricot.workshopsystem.common.dto.RoomDTO;
import apricot.workshopsystem.entityservice.mapper.RoomMapper;
import apricot.workshopsystem.entityservice.model.dao.Room;
import apricot.workshopsystem.entityservice.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoomServiceImpl implements RoomService {
    private static final Logger LOG = LoggerFactory.getLogger(RoomServiceImpl.class);
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    /**
     * Get one room by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RoomDTO> find(final Long id) {
        LOG.debug("Request to get Room : {}", id);

        return roomRepository.findById(id).map(roomMapper::toDto);
    }

    /**
     * Get all the rooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RoomDTO> findAll(final Pageable pageable) {
        LOG.debug("Request to get all Rooms");

        return roomRepository.findAll(pageable).map(roomMapper::toDto);
    }

    /**
     * Save a room.
     *
     * @param roomDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RoomDTO save(final RoomDTO roomDTO) {
        LOG.debug("Request to save Room : {}", roomDTO);

        Room room = roomMapper.toEntity(roomDTO);
        room = roomRepository.save(room);

        return roomMapper.toDto(room);
    }

    /**
     * Delete the room by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(final Long id) {
        LOG.debug("Request to delete Room : {}", id);

        roomRepository.deleteById(id);
    }
}
