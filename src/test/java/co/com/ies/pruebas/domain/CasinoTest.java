package co.com.ies.pruebas.domain;

import static co.com.ies.pruebas.domain.CasinoTestSamples.*;
import static co.com.ies.pruebas.domain.OperatorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import co.com.ies.pruebas.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CasinoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Casino.class);
        Casino casino1 = getCasinoSample1();
        Casino casino2 = new Casino();
        assertThat(casino1).isNotEqualTo(casino2);

        casino2.setId(casino1.getId());
        assertThat(casino1).isEqualTo(casino2);

        casino2 = getCasinoSample2();
        assertThat(casino1).isNotEqualTo(casino2);
    }

    @Test
    void operatorTest() throws Exception {
        Casino casino = getCasinoRandomSampleGenerator();
        Operator operatorBack = getOperatorRandomSampleGenerator();

        casino.setOperator(operatorBack);
        assertThat(casino.getOperator()).isEqualTo(operatorBack);

        casino.operator(null);
        assertThat(casino.getOperator()).isNull();
    }
}
