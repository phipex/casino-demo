package co.com.ies.pruebas.service.mapper;

import static co.com.ies.pruebas.domain.CasinoAsserts.*;
import static co.com.ies.pruebas.domain.CasinoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CasinoMapperTest {

    private CasinoMapper casinoMapper;

    @BeforeEach
    void setUp() {
        casinoMapper = new CasinoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCasinoSample1();
        var actual = casinoMapper.toEntity(casinoMapper.toDto(expected));
        assertCasinoAllPropertiesEquals(expected, actual);
    }
}
