package co.com.ies.pruebas.service.mapper;

import co.com.ies.pruebas.domain.Manufacturer;
import co.com.ies.pruebas.domain.Model;
import co.com.ies.pruebas.service.dto.ManufacturerDTO;
import co.com.ies.pruebas.service.dto.ModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Model} and its DTO {@link ModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModelMapper extends EntityMapper<ModelDTO, Model> {
    @Mapping(target = "manufacturer", source = "manufacturer", qualifiedByName = "manufacturerName")
    ModelDTO toDto(Model s);

    @Named("manufacturerName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ManufacturerDTO toDtoManufacturerName(Manufacturer manufacturer);
}
