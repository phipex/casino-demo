package co.com.ies.pruebas.service.mapper;

import co.com.ies.pruebas.domain.Casino;
import co.com.ies.pruebas.domain.Model;
import co.com.ies.pruebas.domain.Slot;
import co.com.ies.pruebas.service.dto.CasinoDTO;
import co.com.ies.pruebas.service.dto.ModelDTO;
import co.com.ies.pruebas.service.dto.SlotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Slot} and its DTO {@link SlotDTO}.
 */
@Mapper(componentModel = "spring")
public interface SlotMapper extends EntityMapper<SlotDTO, Slot> {
    @Mapping(target = "casino", source = "casino", qualifiedByName = "casinoName")
    @Mapping(target = "model", source = "model", qualifiedByName = "modelName")
    SlotDTO toDto(Slot s);

    @Named("casinoName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CasinoDTO toDtoCasinoName(Casino casino);

    @Named("modelName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ModelDTO toDtoModelName(Model model);
}
