package apricot.workshopsystem.entityservice.mapper;

import apricot.workshopsystem.common.dto.ReservationDTO;
import apricot.workshopsystem.common.util.EntityMapper;
import apricot.workshopsystem.entityservice.model.dao.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {
}
