package apricot.workshopsystem.entityservice.mapper;

import apricot.workshopsystem.common.dto.RoomDTO;
import apricot.workshopsystem.common.util.EntityMapper;
import apricot.workshopsystem.entityservice.model.dao.Room;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
}
