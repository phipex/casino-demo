package co.com.ies.pruebas.service.mapper;

import co.com.ies.pruebas.domain.Operator;
import co.com.ies.pruebas.service.dto.OperatorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operator} and its DTO {@link OperatorDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperatorMapper extends EntityMapper<OperatorDTO, Operator> {}
