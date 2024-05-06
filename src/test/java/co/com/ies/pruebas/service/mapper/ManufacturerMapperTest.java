package co.com.ies.pruebas.service.mapper;

import static co.com.ies.pruebas.domain.ManufacturerAsserts.*;
import static co.com.ies.pruebas.domain.ManufacturerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManufacturerMapperTest {

    private ManufacturerMapper manufacturerMapper;

    @BeforeEach
    void setUp() {
        manufacturerMapper = new ManufacturerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getManufacturerSample1();
        var actual = manufacturerMapper.toEntity(manufacturerMapper.toDto(expected));
        assertManufacturerAllPropertiesEquals(expected, actual);
    }
}
