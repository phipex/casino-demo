package co.com.ies.pruebas.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.pruebas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SlotDTO.class);
        SlotDTO slotDTO1 = new SlotDTO();
        slotDTO1.setId(1L);
        SlotDTO slotDTO2 = new SlotDTO();
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
        slotDTO2.setId(slotDTO1.getId());
        assertThat(slotDTO1).isEqualTo(slotDTO2);
        slotDTO2.setId(2L);
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
        slotDTO1.setId(null);
        assertThat(slotDTO1).isNotEqualTo(slotDTO2);
    }
}
