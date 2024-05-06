package co.com.ies.pruebas.domain;

import static co.com.ies.pruebas.domain.AssertUtils.bigDecimalCompareTo;
import static co.com.ies.pruebas.domain.AssertUtils.zonedDataTimeSameInstant;
import static org.assertj.core.api.Assertions.assertThat;

public class SlotAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSlotAllPropertiesEquals(Slot expected, Slot actual) {
        assertSlotAutoGeneratedPropertiesEquals(expected, actual);
        assertSlotAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSlotAllUpdatablePropertiesEquals(Slot expected, Slot actual) {
        assertSlotUpdatableFieldsEquals(expected, actual);
        assertSlotUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSlotAutoGeneratedPropertiesEquals(Slot expected, Slot actual) {
        assertThat(expected)
            .as("Verify Slot auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSlotUpdatableFieldsEquals(Slot expected, Slot actual) {
        assertThat(expected)
            .as("Verify Slot relevant properties")
            .satisfies(e -> assertThat(e.getIdCasino()).as("check idCasino").isEqualTo(actual.getIdCasino()))
            .satisfies(e -> assertThat(e.getSerial()).as("check serial").isEqualTo(actual.getSerial()))
            .satisfies(e -> assertThat(e.getNuc()).as("check nuc").isEqualTo(actual.getNuc()))
            .satisfies(
                e ->
                    assertThat(e.getInitialized())
                        .as("check initialized")
                        .usingComparator(zonedDataTimeSameInstant)
                        .isEqualTo(actual.getInitialized())
            )
            .satisfies(
                e -> assertThat(e.getBalance()).as("check balance").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getBalance())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSlotUpdatableRelationshipsEquals(Slot expected, Slot actual) {
        assertThat(expected)
            .as("Verify Slot relationships")
            .satisfies(e -> assertThat(e.getCasino()).as("check casino").isEqualTo(actual.getCasino()))
            .satisfies(e -> assertThat(e.getModel()).as("check model").isEqualTo(actual.getModel()));
    }
}