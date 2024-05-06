package co.com.ies.pruebas.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CasinoAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCasinoAllPropertiesEquals(Casino expected, Casino actual) {
        assertCasinoAutoGeneratedPropertiesEquals(expected, actual);
        assertCasinoAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCasinoAllUpdatablePropertiesEquals(Casino expected, Casino actual) {
        assertCasinoUpdatableFieldsEquals(expected, actual);
        assertCasinoUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCasinoAutoGeneratedPropertiesEquals(Casino expected, Casino actual) {
        assertThat(expected)
            .as("Verify Casino auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCasinoUpdatableFieldsEquals(Casino expected, Casino actual) {
        assertThat(expected)
            .as("Verify Casino relevant properties")
            .satisfies(e -> assertThat(e.getNit()).as("check nit").isEqualTo(actual.getNit()))
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDirection()).as("check direction").isEqualTo(actual.getDirection()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCasinoUpdatableRelationshipsEquals(Casino expected, Casino actual) {
        assertThat(expected)
            .as("Verify Casino relationships")
            .satisfies(e -> assertThat(e.getOperator()).as("check operator").isEqualTo(actual.getOperator()));
    }
}