package co.com.ies.pruebas.service.mapper;

import co.com.ies.pruebas.domain.Manufacturer;
import co.com.ies.pruebas.service.dto.ManufacturerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Manufacturer} and its DTO {@link ManufacturerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManufacturerMapper extends EntityMapper<ManufacturerDTO, Manufacturer> {}
