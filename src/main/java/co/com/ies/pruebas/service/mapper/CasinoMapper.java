package co.com.ies.pruebas.service.mapper;

import co.com.ies.pruebas.domain.Casino;
import co.com.ies.pruebas.domain.Operator;
import co.com.ies.pruebas.service.dto.CasinoDTO;
import co.com.ies.pruebas.service.dto.OperatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Casino} and its DTO {@link CasinoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CasinoMapper extends EntityMapper<CasinoDTO, Casino> {
    @Mapping(target = "operator", source = "operator", qualifiedByName = "operatorName")
    CasinoDTO toDto(Casino s);

    @Named("operatorName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    OperatorDTO toDtoOperatorName(Operator operator);
}
