package co.com.ies.pruebas.service.mapper;

import static co.com.ies.pruebas.domain.ModelAsserts.*;
import static co.com.ies.pruebas.domain.ModelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModelMapperTest {

    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getModelSample1();
        var actual = modelMapper.toEntity(modelMapper.toDto(expected));
        assertModelAllPropertiesEquals(expected, actual);
    }
}
