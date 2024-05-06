package co.com.ies.pruebas.domain;

import static co.com.ies.pruebas.domain.CasinoTestSamples.*;
import static co.com.ies.pruebas.domain.ModelTestSamples.*;
import static co.com.ies.pruebas.domain.SlotTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.pruebas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SlotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Slot.class);
        Slot slot1 = getSlotSample1();
        Slot slot2 = new Slot();
        assertThat(slot1).isNotEqualTo(slot2);

        slot2.setId(slot1.getId());
        assertThat(slot1).isEqualTo(slot2);

        slot2 = getSlotSample2();
        assertThat(slot1).isNotEqualTo(slot2);
    }

    @Test
    void casinoTest() throws Exception {
        Slot slot = getSlotRandomSampleGenerator();
        Casino casinoBack = getCasinoRandomSampleGenerator();

        slot.setCasino(casinoBack);
        assertThat(slot.getCasino()).isEqualTo(casinoBack);

        slot.casino(null);
        assertThat(slot.getCasino()).isNull();
    }

    @Test
    void modelTest() throws Exception {
        Slot slot = getSlotRandomSampleGenerator();
        Model modelBack = getModelRandomSampleGenerator();

        slot.setModel(modelBack);
        assertThat(slot.getModel()).isEqualTo(modelBack);

        slot.model(null);
        assertThat(slot.getModel()).isNull();
    }
}
