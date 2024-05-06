package co.com.ies.pruebas.domain;

import static co.com.ies.pruebas.domain.ManufacturerTestSamples.*;
import static co.com.ies.pruebas.domain.ModelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.pruebas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Model.class);
        Model model1 = getModelSample1();
        Model model2 = new Model();
        assertThat(model1).isNotEqualTo(model2);

        model2.setId(model1.getId());
        assertThat(model1).isEqualTo(model2);

        model2 = getModelSample2();
        assertThat(model1).isNotEqualTo(model2);
    }

    @Test
    void manufacturerTest() throws Exception {
        Model model = getModelRandomSampleGenerator();
        Manufacturer manufacturerBack = getManufacturerRandomSampleGenerator();

        model.setManufacturer(manufacturerBack);
        assertThat(model.getManufacturer()).isEqualTo(manufacturerBack);

        model.manufacturer(null);
        assertThat(model.getManufacturer()).isNull();
    }
}
