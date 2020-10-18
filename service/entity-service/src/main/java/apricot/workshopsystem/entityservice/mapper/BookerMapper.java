package apricot.workshopsystem.entityservice.mapper;

import apricot.workshopsystem.common.dto.BookerDTO;
import apricot.workshopsystem.common.util.EntityMapper;
import apricot.workshopsystem.entityservice.model.dao.Booker;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookerMapper extends EntityMapper<BookerDTO, Booker> {
}
