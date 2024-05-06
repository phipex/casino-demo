package co.com.ies.pruebas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.pruebas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CasinoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CasinoDTO.class);
        CasinoDTO casinoDTO1 = new CasinoDTO();
        casinoDTO1.setId(1L);
        CasinoDTO casinoDTO2 = new CasinoDTO();
        assertThat(casinoDTO1).isNotEqualTo(casinoDTO2);
        casinoDTO2.setId(casinoDTO1.getId());
        assertThat(casinoDTO1).isEqualTo(casinoDTO2);
        casinoDTO2.setId(2L);
        assertThat(casinoDTO1).isNotEqualTo(casinoDTO2);
        casinoDTO1.setId(null);
        assertThat(casinoDTO1).isNotEqualTo(casinoDTO2);
    }
}
